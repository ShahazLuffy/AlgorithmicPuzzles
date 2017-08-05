package com.example.shahaz.algorithmicpuzzles;

public class Puzzle {
    private int id=-1;
    private String hint = "hint";
    private String intro = "intro";
    private String notation = "notation";
    private String title = "title";
    private String trivia = "trivia";
    private String icon = "icon";
    private String imageAddress = "imageAddress";
    private int hardness;

    public int getId(){return id;}

    public void setId(int id){this.id=id;}

    public String getHint(){return hint;}

    public void setHint(String hint){this.hint=hint;}

    public String getIntro(){return intro;}

    public void setIntro(String intro){this.intro=intro;}

    public String getNotation(){return notation;}

    public void setNotation(String notation){this.notation=notation;}

    public String getTitle(){return title;}

    public void setTitle(String title){this.title=title;}

    public String getTrivia(){return trivia;}

    public void setTrivia(String trivia){this.trivia=trivia;}

    public String getIcon(){return icon;}

    public void setIcon(String icon){this.icon=icon;}

    public String getImageAddress(){return imageAddress;}

    public void setImageAddress(String imageAddress){this.imageAddress=imageAddress;}

    public int getHardness(){return hardness;}

    public void setHardness(int hardness){this.hardness=hardness;}

}
