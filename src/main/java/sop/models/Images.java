package sop.models;

public class Images {

    private int imageID;            // Unique ID for the image
    private String imageName;       // Name of the image
    private int mainStatus;         // Status of the image
    private int serviceID;          // ID of the related service (cannot be null)
    private int userID;             // ID of the user who performed the survey (cannot be null)
    private int postID;             // ID of the post (cannot be null)
    private int bannerID;           // ID of the banner (cannot be null)
    private int contractItemID;     // ID of the contract item
    private String status;          // Status of the image (active/inactive)

    // Constructor
    public Images(int imageID, String imageName, int mainStatus, int serviceID, int userID, int postID, int bannerID, int contractItemID, String status) {
        this.imageID = imageID;
        this.imageName = imageName;
        this.mainStatus = mainStatus;
        this.serviceID = serviceID;
        this.userID = userID;
        this.postID = postID;
        this.bannerID = bannerID;
        this.contractItemID = contractItemID;
        this.status = status;
    }
    public Images() {
    	
    }

    // Getter and Setter methods

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getMainStatus() {
        return mainStatus;
    }

    public void setMainStatus(int mainStatus) {
        this.mainStatus = mainStatus;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getBannerID() {
        return bannerID;
    }

    public void setBannerID(int bannerID) {
        this.bannerID = bannerID;
    }

    public int getContractItemID() {
        return contractItemID;
    }

    public void setContractItemID(int contractItemID) {
        this.contractItemID = contractItemID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
