package org.rpinzon.snapper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Renan T. Pinzon on 09/08/18.
 */
public class Data {

    private Integer pk;

    private Integer score;

    public Data() {
        super();
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "{ \"pk\":" + getPk() + ", \"score\": \"" + getScore() + "\" }";
    }

}
