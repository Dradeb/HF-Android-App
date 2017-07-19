package ma.dradeb.hiddenfounders.Modals;

import java.io.Serializable;

/**
 * Created by Youness on 09/07/2017.
 */

public class Album implements Serializable{

    String id, name , creationDate , coverUrl , count;

    public Album(String id, String name, String creationDate, String coverUrl) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.coverUrl = coverUrl;
    }

    public Album() { }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setCoverUrl(String coverUrl)
    {
        this.coverUrl = coverUrl ;
    }

    public String getCoverUrl()
    {
        return this.coverUrl ;
    }

    public String getCount() {return count;}

    public void setCount(String count) { this.count = count;}
}
