package com.valorant.api.dtos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SkinDTO {


    public int status;
    public List<DataItem> data;


    public static class DataItem {
        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayIcon() {
            return displayIcon;
        }

        public void setDisplayIcon(String displayIcon) {
            this.displayIcon = displayIcon;
        }

        public String getFullRender() {
            return fullRender;
        }

        public void setFullRender(String fullRender) {
            this.fullRender = fullRender;
        }

        public String getSwatch() {
            return swatch;
        }

        public void setSwatch(String swatch) {
            this.swatch = swatch;
        }

        public String getStreamedVideo() {
            return streamedVideo;
        }

        public void setStreamedVideo(String streamedVideo) {
            this.streamedVideo = streamedVideo;
        }

        public String getAssetPath() {
            return assetPath;
        }

        public void setAssetPath(String assetPath) {
            this.assetPath = assetPath;
        }

        public String uuid;
        public String displayName;

        @Override
        public String toString() {
            return "DataItem{" +
                    "uuid='" + uuid + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", displayIcon='" + displayIcon + '\'' +
                    ", fullRender='" + fullRender + '\'' +
                    ", swatch='" + swatch + '\'' +
                    ", streamedVideo='" + streamedVideo + '\'' +
                    ", assetPath='" + assetPath + '\'' +
                    '}';
        }

        public String displayIcon;
        public String fullRender;
        public String swatch;
        public String streamedVideo;
        public String assetPath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SkinDTO{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}


