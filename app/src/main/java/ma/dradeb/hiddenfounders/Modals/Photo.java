package ma.dradeb.hiddenfounders.Modals;

/**
 * Created by Youness on 10/07/2017.
 */

public class Photo {

    String id , source ,thumb ;
    boolean checked ;

    public Photo(String id, String source, String thumb) {
        this.id = id;
        this.source = source;
        this.thumb = thumb;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Photo(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
