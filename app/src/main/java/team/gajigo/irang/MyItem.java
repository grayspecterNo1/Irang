package team.gajigo.irang;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {

    private final LatLng mPosition;
    private final String sname;

    public MyItem(double lat, double lng, String sname){
        this.sname = sname;
        this.mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    @Override
    public String getTitle() {
        return sname;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}

