package com.gpse.basis.services;

import com.gpse.basis.domain.VelodynePoint;

import java.awt.image.BufferedImage;
import java.util.List;

public interface RosBagService {
    void saveCameraImagesForTrack(int trackId, String filename);

    List<BufferedImage> getInfraRedImagesForTrack(int trackId);

    List<List<VelodynePoint>> getVelodynePointsForTrack(int trackId);

    List<String> getImagesForTrack(int trackId);

}
