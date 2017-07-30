package darkrai.lysis.corp.aidbot;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class BurnsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burns);
        VideoView view = (VideoView)findViewById(R.id.burns);
        String path = "android.resource://darkrai.lysis.corp.aidbot"+"/"+R.raw.burns;
        view.setVideoURI(Uri.parse(path));
        view.start();

    }
}
