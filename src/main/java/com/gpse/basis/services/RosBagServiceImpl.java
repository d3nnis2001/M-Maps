package com.gpse.basis.services;

import com.github.swrirobotics.bags.reader.BagFile;
import com.github.swrirobotics.bags.reader.BagReader;
import com.github.swrirobotics.bags.reader.MessageHandler;
import com.github.swrirobotics.bags.reader.TopicInfo;
import com.github.swrirobotics.bags.reader.exceptions.BagReaderException;
import com.github.swrirobotics.bags.reader.exceptions.UninitializedFieldException;
import com.github.swrirobotics.bags.reader.messages.serialization.*;
import com.github.swrirobotics.bags.reader.records.Connection;
import com.gpse.basis.domain.CameraImage;
import com.gpse.basis.domain.DataSet;
import com.gpse.basis.domain.VelodynePoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


@Service
public class RosBagServiceImpl implements RosBagService{


    private final MongoTemplate template;

    private static final String IMAGE_DIRECTORY = "../gp-se-ss-2024-team1-2/rosbagPictures";

    private static final String directory_name = "/rosbagPictures";



    @Autowired
    RosBagServiceImpl(MongoTemplate tmp) {
        template = tmp;
    }

    @Override
    public List<CameraImage> saveCameraImagesForTrack(int trackId, String filename) {
        List<CameraImage> cameraImagelst = new ArrayList<>();
        Lock lock = new ReentrantLock();
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        List<Worker> workers = new ArrayList<>();
            try {
                BagFile f = BagReader.readFile(filename);
                for (TopicInfo topic : f.getTopics()) {
                    System.out.println(topic.getName());
                    if (Objects.equals(topic.getName(), "/ColourCam/image_raw")) {
                        long count = topic.getMessageCount();
                        System.out.println("Message-count: " + count);

                        for(int i = 0; i < count; ++i) {
                            workers.add(new Worker(filename, cameraImagelst, lock, i, trackId));
                            executorService.submit(workers.getLast());
                        }

                        executorService.shutdown();
                        try {
                            var t = executorService.awaitTermination(60, TimeUnit.SECONDS);
                            System.out.println(t);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    }

            } catch (BagReaderException e) {
                throw new RuntimeException(e);
            }
        return cameraImagelst;
    }

    @Override
    public List<CameraImage> saveInfraRedImagesForTrack(int trackId, String fileName) {
        List<CameraImage> imageList = new ArrayList<>();
            try {
                BagFile f = BagReader.readFile(fileName);
                for (TopicInfo topic : f.getTopics()) {
                    if (Objects.equals(topic.getName(), "/IRCam/image_raw")) {
                        AtomicInteger xyz = new AtomicInteger();
                        f.forMessagesOnTopic(topic.getName(), (message, connection) -> {
                            /*
                            MessageType header = message.getField("header");
                            header.getFieldNames().forEach(System.out::println);
                            System.out.println();
                            message.getFieldNames().forEach(System.out::println);
                            System.out.println();
                             */
                            try {
                                long width = message.<UInt32Type>getField("width").getValue();
                                long height = message.<UInt32Type>getField("height").getValue();
                                var lst = message.<ArrayType>getField("data");
                                var a = lst.getAsBytes();
                                int index = 0;
                                BufferedImage irImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
                                for (int i = 0; i < height; ++i) {
                                    for (int j = 0; j < width; ++j) {
                                        int value = ((int) a[index++]) & 0xff;
                                        irImage.setRGB(j, i, new Color(value, value, value).getRGB());
                                    }
                                }
                                long unixTimestamp = Instant.now().getEpochSecond();
                                String name = "/IRCameraImage" + "-" + unixTimestamp + xyz + ".png";
                                File outputfile = new File(IMAGE_DIRECTORY + name);
                                xyz.getAndIncrement();
                                try {
                                    ImageIO.write(irImage, "png", outputfile);
                                    imageList.add(new CameraImage(trackId, "http://localhost:8080" + directory_name + name , 1, "", xyz.get()));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } catch (UninitializedFieldException e) {
                                throw new RuntimeException(e);
                            }

                            return true;
                        });
                    }
                }
            } catch (BagReaderException e) {
                throw new RuntimeException(e);
            }
        return imageList;
    }

    @Override
    public List<List<VelodynePoint>> getVelodynePointsForTrack(int trackId) {
        List<List<VelodynePoint>> pointList = new ArrayList<>();
        Query q = new Query();
        q.addCriteria(Criteria.where("streckenId").is(trackId));
        var data = template.find(q, DataSet.class);
        data.stream().filter(dt -> dt.getFileName().contains("velodyne")).toList();
        for(var d : data) {
            try {
                BagFile f = BagReader.readFile(d.getFileName());
                for (TopicInfo topic : f.getTopics()) {
                    if (Objects.equals(topic.getName(), "/velodyne_points")) {
                        f.forMessagesOnTopic(topic.getName(), (message, connection) -> {
                            List<String> fields = message.getFieldNames();
                            //fields.forEach(System.out::println);
                            //System.out.println();
                            List<VelodynePoint> klm = new ArrayList<>();
                            try {
                                long point_step = message.<UInt32Type>getField("point_step").getValue();
                                //System.out.println("Point_step: " + point_step);
                                var fld = message.<ArrayType>getField("fields").getFields();
                                int offset_x = 0, offset_y = 0, offset_z = 0, offset_intensity = 0, offset_time = 0;
                                for (Field pointField : fld) {
                                    MessageType pointMsg = (MessageType) pointField;
                                    if(Objects.equals(pointMsg.<StringType>getField("name").getValue(), "x")) {
                                        offset_x = Math.toIntExact(pointMsg.<UInt32Type>getField("offset").getValue());
                                       // System.out.println("Offset_x: " + offset_x);
                                    }
                                    else if(Objects.equals(pointMsg.<StringType>getField("name").getValue(), "y")) {
                                        offset_y = Math.toIntExact(pointMsg.<UInt32Type>getField("offset").getValue());
                                       // System.out.println("Offset_y: " + offset_y);
                                    }
                                    else if(Objects.equals(pointMsg.<StringType>getField("name").getValue(), "z")) {
                                        offset_z = Math.toIntExact(pointMsg.<UInt32Type>getField("offset").getValue());
                                       // System.out.println("Offset_z: " + offset_z);
                                    }
                                    else if(Objects.equals(pointMsg.<StringType>getField("name").getValue(), "intensity")) {
                                        offset_intensity = Math.toIntExact(pointMsg.<UInt32Type>getField("offset").getValue());
                                       // System.out.println("Offset_z: " + offset_intensity);
                                    }
                                    else if(Objects.equals(pointMsg.<StringType>getField("name").getValue(), "time")) {
                                        offset_time = Math.toIntExact(pointMsg.<UInt32Type>getField("offset").getValue());
                                        //System.out.println("Offset_time: " + offset_time);
                                    }
                                }
                                var lst = message.<ArrayType>getField("data");
                                byte[] a = lst.getAsBytes();
                                int size = 0;
                                    for(int i = 0; i < a.length; i+=22){
                                        byte[] x_value = new byte[] {a[i+offset_x], a[i+offset_x+1], a[i+offset_x+2], a[i+offset_x+3]};
                                        byte[] y_value = new byte[] {a[i+offset_y], a[i+offset_y+1], a[i+offset_y+2], a[i+offset_y+3]};
                                        byte[] z_value = new byte[] {a[i+offset_z], a[i+offset_z+1], a[i+offset_z+2], a[i+offset_z+3]};
                                        byte[] i_value = new byte[] {a[i+offset_intensity], a[i+offset_intensity+1], a[i+offset_intensity+2], a[i+offset_intensity+3]};
                                        byte[] t_value = new byte[] {a[i+offset_time], a[i+offset_time+1], a[i+offset_time+2], a[i+offset_time+3]};

                                        float x = byteArrayToFloat(x_value, ByteOrder.LITTLE_ENDIAN);
                                        float y = byteArrayToFloat(y_value, ByteOrder.LITTLE_ENDIAN);
                                        float z = byteArrayToFloat(z_value, ByteOrder.LITTLE_ENDIAN);
                                        float intensity = byteArrayToFloat(i_value, ByteOrder.LITTLE_ENDIAN);
                                        float time = byteArrayToFloat(t_value, ByteOrder.LITTLE_ENDIAN);
                                        klm.add(new VelodynePoint(x, y, z, intensity));
                                        size++;
                                    }
                                //System.out.println("Size of Points: " + size);
                            } catch (UninitializedFieldException e) {
                                throw new RuntimeException(e);
                            }
                            pointList.add(klm);
                            return true;
                        });
                    }
                }
            } catch (BagReaderException e) {
                throw new RuntimeException(e);
            }
        }

        return pointList;
    }

    @Override
    public List<String> getImagesForTrack(int trackId) {
        List<String> lst = new ArrayList<>();
        Query q = new Query();
        q.addCriteria(Criteria.where("track_id").is(trackId));
        q.addCriteria(Criteria.where("type").is(0));
        var itr = template.find(q, CameraImage.class);
        var sort_itr = itr.parallelStream().sorted(Comparator.comparingInt(CameraImage::getIndex)).toList();
        sort_itr.forEach(w -> lst.add(w.getPath()));
        return lst;
    }

    @Override
    public List<String> getIRImagesForTrack(int trackId) {
        List<String> lst = new ArrayList<>();
        Query q = new Query();
        q.addCriteria(Criteria.where("track_id").is(trackId));
        q.addCriteria(Criteria.where("type").is(1));
        var itr = template.find(q, CameraImage.class);
        itr.forEach(w -> lst.add(w.getPath()));
        return lst;
    }


    private boolean inRange(int x, int y) {
        return x >= 0 && x < 3008 && y >= 0 && y < 4112;
    }

    private class Worker implements Runnable {

        private final String filename;

        List<CameraImage> cameraImagesList;
        Lock lock;
        int xyz;

        int trackId;
        public Worker(String filename, List<CameraImage> cameraImagesList, Lock lock, int xyz, int trackId) {
            this.filename = filename;
            this.cameraImagesList = cameraImagesList;
            this.lock = lock;
            this.xyz = xyz;
            this.trackId = trackId;
        }
        @Override
        public void run() {
            try {
                BagFile f = BagReader.readFile(filename);
                MessageType message = f.getMessageOnTopicAtIndex("/ColourCam/image_raw", xyz);
                long width = message.<UInt32Type>getField("width").getValue();
                long height = message.<UInt32Type>getField("height").getValue();
                var lst = message.<ArrayType>getField("data");
                var a = lst.getAsBytes();
                BufferedImage image = demosaic(a, (int) width, (int) height);
                long unixTimestamp = Instant.now().getEpochSecond();
                String name = "/BagCameraImage" + "-" + unixTimestamp + xyz + ".png";
                File outputfile = new File(IMAGE_DIRECTORY + name);
                try {
                    ImageIO.write(image, "png", outputfile);
                    lock.lock();
                    cameraImagesList.add(new CameraImage(trackId, "http://localhost:8080" + directory_name + name , 0, "", xyz));
                    lock.unlock();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException(e);
                }

            } catch (UninitializedFieldException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println("Exception: ");
                e.printStackTrace();
            } catch(Error e) {
                System.out.println("Error: ");
                e.printStackTrace();
            }
        }
    }

  /*
    Demosaic algorithm loosely based on:
    D. Wang, G. Yu, X. Zhou, C. Wang; Image Demosaicking for Bayer-patterned CFA Images Using Improved Linear Interpolation; IEEE explore
   */
    private BufferedImage demosaic(byte[] a, int width, int height) {
        int[][] pic = new int[(int) height][(int) width];
        int index = 0;
        BufferedImage rgbImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                pic[i][j] = ((int) a[index++]) & 0xff;
            }
        }

        for (int i = 0; i < height; ++i) {
            //red row
            if (i % 2 == 0) {
                for (int j = 0; j < width; ++j) {
                    //red tile
                    if (j % 2 == 0) {
                        double green = 0.5 * pic[i][j] +
                            0.25 * (inRange(i, j - 1) ? pic[i][j - 1] : 0.0) +
                            0.25 * (inRange(i, j + 1) ? pic[i][j + 1] : 0.0) +
                            0.25 * (inRange(i - 1, j) ? pic[i - 1][j] : 0.0) +
                            0.25 * (inRange(i + 1, j) ? pic[i + 1][j] : 0.0) -
                            0.125 * (inRange(i, j - 2) ? pic[i][j - 2] : 0.0) -
                            0.125 * (inRange(i, j + 2) ? pic[i][j + 2] : 0.0) -
                            0.125 * (inRange(i - 2, j) ? pic[i - 2][j] : 0.0) -
                            0.125 * (inRange(i + 2, j) ? pic[i + 2][j] : 0.0);
                        double blue = 0.75 * pic[i][j] +
                            0.25 * (inRange(i - 1, j - 1) ? pic[i - 1][j - 1] : 0.0) +
                            0.25 * (inRange(i + 1, j - 1) ? pic[i + 1][j - 1] : 0.0) +
                            0.25 * (inRange(i - 1, j + 1) ? pic[i - 1][j + 1] : 0.0) +
                            0.25 * (inRange(i + 1, j + 1) ? pic[i + 1][j + 1] : 0.0) -
                            (3.0 / 16.0) * (inRange(i, j - 2) ? pic[i][j - 2] : 0.0) -
                            (3.0 / 16.0) * (inRange(i, j + 2) ? pic[i][j + 2] : 0.0) -
                            (3.0 / 16.0) * (inRange(i - 2, j) ? pic[i - 2][j] : 0.0) -
                            (3.0 / 16.0) * (inRange(i + 2, j) ? pic[i + 2][j] : 0.0);
                        int grn = Math.max(0, Math.min((int) Math.round(green), 255));
                        int blu = Math.max(0, Math.min((int) Math.round(blue), 255));
                        rgbImage.setRGB(j, i, new Color(pic[i][j], grn, blu).getRGB());

                    }
                    //green tile
                    else {
                        double red = 5.0 / 8.0 * pic[i][j] +
                            0.5 * (inRange(i, j - 1) ? pic[i][j - 1] : 0.0) +
                            0.5 * (inRange(i, j + 1) ? pic[i][j + 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i - 1, j - 1) ? pic[i - 1][j - 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i + 1, j - 1) ? pic[i + 1][j - 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i - 1, j + 1) ? pic[i - 1][j + 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i + 1, j + 1) ? pic[i + 1][j + 1] : 0.0);

                        double blue = 5.0 / 8.0 * pic[i][j] +
                            0.5 * (inRange(i - 1, j) ? pic[i - 1][j] : 0.0) +
                            0.5 * (inRange(i + 1, j) ? pic[i + 1][j] : 0.0) -
                            5.0 / 32.0 * (inRange(i - 1, j - 1) ? pic[i - 1][j - 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i + 1, j - 1) ? pic[i + 1][j - 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i - 1, j + 1) ? pic[i - 1][j + 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i + 1, j + 1) ? pic[i + 1][j + 1] : 0.0);


                        int rd = Math.max(0, Math.min((int) Math.round(red), 255));
                        int blu = Math.max(0, Math.min((int) Math.round(blue), 255));
                        rgbImage.setRGB(j, i, new Color(rd, pic[i][j], blu).getRGB());

                    }
                }
            }
            //blue row
            else {
                for (int j = 0; j < width; ++j) {
                    //green tile
                    if (j % 2 == 0) {

                        double blue = 5.0 / 8.0 * pic[i][j] +
                            0.5 * (inRange(i, j - 1) ? pic[i][j - 1] : 0.0) +
                            0.5 * (inRange(i, j + 1) ? pic[i][j + 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i - 1, j - 1) ? pic[i - 1][j - 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i + 1, j - 1) ? pic[i + 1][j - 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i - 1, j + 1) ? pic[i - 1][j + 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i + 1, j + 1) ? pic[i + 1][j + 1] : 0.0);

                        double red = 5.0 / 8.0 * pic[i][j] +
                            0.5 * (inRange(i - 1, j) ? pic[i - 1][j] : 0.0) +
                            0.5 * (inRange(i + 1, j) ? pic[i + 1][j] : 0.0) -
                            5.0 / 32.0 * (inRange(i - 1, j - 1) ? pic[i - 1][j - 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i + 1, j - 1) ? pic[i + 1][j - 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i - 1, j + 1) ? pic[i - 1][j + 1] : 0.0) -
                            5.0 / 32.0 * (inRange(i + 1, j + 1) ? pic[i + 1][j + 1] : 0.0);


                        int rd = Math.max(0, Math.min((int) Math.round(red), 255));
                        int blu = Math.max(0, Math.min((int) Math.round(blue), 255));
                        rgbImage.setRGB(j, i, new Color(rd, pic[i][j], blu).getRGB());

                    }
                    //blue tile
                    else {
                        double green = 0.5 * pic[i][j] +
                            0.25 * (inRange(i, j - 1) ? pic[i][j - 1] : 0.0) +
                            0.25 * (inRange(i, j + 1) ? pic[i][j + 1] : 0.0) +
                            0.25 * (inRange(i - 1, j) ? pic[i - 1][j] : 0.0) +
                            0.25 * (inRange(i + 1, j) ? pic[i + 1][j] : 0.0) -
                            0.125 * (inRange(i, j - 2) ? pic[i][j - 2] : 0.0) -
                            0.125 * (inRange(i, j + 2) ? pic[i][j + 2] : 0.0) -
                            0.125 * (inRange(i - 2, j) ? pic[i - 2][j] : 0.0) -
                            0.125 * (inRange(i + 2, j) ? pic[i + 2][j] : 0.0);

                        double red = 0.75 * pic[i][j] +
                            0.25 * (inRange(i - 1, j - 1) ? pic[i - 1][j - 1] : 0.0) +
                            0.25 * (inRange(i + 1, j - 1) ? pic[i + 1][j - 1] : 0.0) +
                            0.25 * (inRange(i - 1, j + 1) ? pic[i - 1][j + 1] : 0.0) +
                            0.25 * (inRange(i + 1, j + 1) ? pic[i + 1][j + 1] : 0.0) -
                            3.0 / 16.0 * (inRange(i, j - 2) ? pic[i][j - 2] : 0.0) -
                            3.0 / 16.0 * (inRange(i, j + 2) ? pic[i][j + 2] : 0.0) -
                            3.0 / 16.0 * (inRange(i - 2, j) ? pic[i - 2][j] : 0.0) -
                            3.0 / 16.0 * (inRange(i + 2, j) ? pic[i + 2][j] : 0.0);


                        int grn = Math.max(0, Math.min((int) Math.round(green), 255));
                        int rd = Math.max(0, Math.min((int) Math.round(red), 255));
                        rgbImage.setRGB(j, i, new Color(rd, grn, pic[i][j]).getRGB());

                    }
                }

            }
        }
        return rgbImage;
    }

    public float byteArrayToFloat(byte[] bytes, ByteOrder byteOrder) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(byteOrder);
        return buffer.getFloat();
    }

}
