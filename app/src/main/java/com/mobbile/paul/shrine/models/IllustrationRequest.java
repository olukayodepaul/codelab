package com.mobbile.paul.shrine.models;

import java.io.Serializable;

public class IllustrationRequest implements Serializable {

    private String content;

    private Boolean isHaveCharacter;

    private String characterImage;

    public IllustrationRequest() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsHaveCharacter() {
        return isHaveCharacter;
    }

    public void setIsHaveCharacter(Boolean isHaveCharacter) {
        this.isHaveCharacter = isHaveCharacter;
    }

    public String getCharacterImage() {
        return characterImage;
    }

    public void setCharacterImage(String characterImage) {
        this.characterImage = characterImage;
    }
}
