package team.gajigo.irang;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CourseTabFragment01 extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        ImageView imageView = (ImageView) getView().findViewById(R.id.nowoncourse);
        return inflater.inflate(R.layout.course_tab_fragment01, container, false);
    }

}
