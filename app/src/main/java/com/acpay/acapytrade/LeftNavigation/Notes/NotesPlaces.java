package com.acpay.acapytrade.LeftNavigation.Notes;

import com.acpay.acapytrade.Cordinations.ECCardData;
import com.acpay.acapytrade.R;

import java.util.List;
import java.util.Random;

public class NotesPlaces implements ECCardData<NotesPlacesDetails> {
    private String placeId;
    private String palaceName;
    private String palaceLocation;
    private String palaceDetails;
    private Integer headBackgroundResource;
    private Integer mainBackgroundResource;
    private List<NotesPlacesDetails> listItems;

    public NotesPlaces() {
    }

    public NotesPlaces(String placeId,String palaceName, String palaceLocation, String palaceDetails, Integer headBackgroundResource, Integer mainBackgroundResource, List<NotesPlacesDetails> listItems) {
        this.placeId = placeId;
        this.palaceName = palaceName;
        this.palaceLocation = palaceLocation;
        this.palaceDetails = palaceDetails;
        this.headBackgroundResource = headBackgroundResource;
        this.mainBackgroundResource = mainBackgroundResource;
        this.listItems = listItems;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPalaceName() {
        return palaceName;
    }

    public void setPalaceName(String palaceName) {
        this.palaceName = palaceName;
    }

    public String getPalaceLocation() {
        return palaceLocation;
    }

    public void setPalaceLocation(String palaceLocation) {
        this.palaceLocation = palaceLocation;
    }

    public String getPalaceDetails() {
        return palaceDetails;
    }

    public void setPalaceDetails(String palaceDetails) {
        this.palaceDetails = palaceDetails;
    }

    @Override
    public Integer getHeadBackgroundResource() {
        return headBackgroundResource;
    }

    public void setHeadBackgroundResource(Integer headBackgroundResource) {
        this.headBackgroundResource = headBackgroundResource;
    }

    @Override
    public Integer getMainBackgroundResource() {
        return mainBackgroundResource;
    }

    public void setMainBackgroundResource(Integer mainBackgroundResource) {
        this.mainBackgroundResource = mainBackgroundResource;
    }

    @Override
    public List<NotesPlacesDetails> getListItems() {
        return listItems;
    }

    public void setListItems(List<NotesPlacesDetails> listItems) {
        this.listItems = listItems;
    }

    public int getImage() {
        return R.drawable.ic_baseline_airplay_24;
    }
}
