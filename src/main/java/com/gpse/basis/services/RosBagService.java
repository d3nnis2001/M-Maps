package com.gpse.basis.services;

import com.gpse.basis.domain.CameraImage;
import com.gpse.basis.domain.VelodynePoint;

import java.awt.image.BufferedImage;
import java.util.List;

public interface RosBagService {
    List<CameraImage> saveCameraImagesForTrack(int trackId, String filename);

    List<CameraImage> saveInfraRedImagesForTrack(int trackId, String filename);

    List<List<VelodynePoint>> getVelodynePointsForTrack(int trackId);

    List<String> getImagesForTrack(int trackId);

    List<String> getIRImagesForTrack(int trackId);

}
