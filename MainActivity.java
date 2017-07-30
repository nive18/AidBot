package darkrai.lysis.corp.aidbot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
//import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;

import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.RecognizeCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

public class MainActivity extends AppCompatActivity {
//Button textspeech = (Button)findViewById(R.id.textspeech);
    public static final String TAG = "MainActivity";
    public Button search;
    StreamPlayer streamPlayer;
   // private RecyclerView recyclerView;
    private SpeechToText speechService;

    private boolean listening = false;
    private MicrophoneInputStream capture;
    private EditText inputMessage;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int RECORD_REQUEST_CODE = 101;
    private boolean permissionToRecordAccepted = false;
    private boolean initialRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Multidex.install(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = (Button) findViewById(R.id.speech);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordMessage();
            }
        });
        inputMessage = (EditText) findViewById(R.id.message);
        Button burns = (Button) findViewById(R.id.burns);
        burns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent burn = new Intent(MainActivity.this,
                        darkrai.lysis.corp.aidbot.BurnsActivity.class);
                startActivity(burn);
            }
        });
        Button accidents = (Button) findViewById(R.id.accidents);
        accidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accidents = new Intent(MainActivity.this,
                        darkrai.lysis.corp.aidbot.AccidentActivity.class);
                startActivity(accidents);
            }
        });
        Button suffocation = (Button) findViewById(R.id.suffocation);
        suffocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent suffocation = new Intent(MainActivity.this, darkrai.lysis.corp.aidbot.SuffocationActivity.class);
                startActivity(suffocation);
            }
        });
        Button frost = (Button) findViewById(R.id.frost);
        frost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frost = new Intent(MainActivity.this, darkrai.lysis.corp.aidbot.FrostActivity.class);
                startActivity(frost);
            }
        });
        Button faint = (Button) findViewById(R.id.faint);
        faint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent faint = new Intent(MainActivity.this, darkrai.lysis.corp.aidbot.FaintActivity.class);
                startActivity(faint);
            }
        });
        Button shock = (Button) findViewById(R.id.shock);
        shock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shock = new Intent(MainActivity.this, darkrai.lysis.corp.aidbot.ShockActivity.class);
                startActivity(shock);
            }
        });
       /* final TextToSpeech service = new TextToSpeech();
        service.setUsernameAndPassword("1e482070-f947-44b2-8e59-cd6095efaef9", "5Pfd5enFNKcb");
        View includeLayout = findViewById(R.id.id1);
        Button textSpeech = (Button)includeLayout.findViewById(R.id.textspeech);*/
        //speech to text
        // -----------------------------------------------------------------
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            makeRequest();
        }
    }
       //----------------------------------------------------------
       //text to speech----------------------------------------------
        /*recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        Message audioMessage;
                        try {

                            audioMessage =(Message) messageArrayList.get(position);
                            streamPlayer = new StreamPlayer();
                            if(audioMessage != null && !audioMessage.getMessage().isEmpty())
                                //Change the Voice format and choose from the available choices
                                streamPlayer.playStream(service.synthesize(audioMessage.getMessage(), Voice.EN_LISA).execute());
                            else
                                streamPlayer.playStream(service.synthesize("No Text Specified", Voice.EN_LISA).execute());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }

            @Override
            public void onLongClick(View view, int position) {
                recordMessage();

            }
        }));

        textspeech.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(checkInternetConnection()) {
                    sendMessage();
                }
            }
        });

        textspeech.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                recordMessage();
            }
        });
    };*/
    //Text to speech-----------------------------------------------------
    //Permission to speech to text
    //@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case RECORD_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
        }
        if (!permissionToRecordAccepted ) finish();

    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_REQUEST_CODE);
    }



    //For Speech to text Check mic permission
    public void recordMessage() {
        //mic.setEnabled(false);
        speechService = new SpeechToText();
        speechService.setUsernameAndPassword("d11dfca8-2d92-4cf3-8246-cb389d6984cc", "P4VvQrk35Yqw");

        if(listening != true) {
            capture = new MicrophoneInputStream(true);
            new Thread(new Runnable() {
                @Override public void run() {
                    try {
                        speechService.recognizeUsingWebSocket(capture, getRecognizeOptions(), new MicrophoneRecognizeDelegate());
                    } catch (Exception e) {
                        showError(e);
                    }
                }
            }).start();
            listening = true;
            Toast.makeText(MainActivity.this,"Listening....Click to Stop", Toast.LENGTH_LONG).show();

        } else {
            try {
                capture.close();
                listening = false;
                Toast.makeText(MainActivity.this,"Stopped Listening....Click to Start", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //Record a message via Watson Speech to Text

    }

    /**
     * Check Internet Connection
     * @return
     */
    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Check for network connections
        if (isConnected){
            return true;
        }
        else {
            Toast.makeText(this, " No Internet Connection available ", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    //Private Methods - Speech to Text
    private RecognizeOptions getRecognizeOptions() {
        return new RecognizeOptions.Builder()
                .continuous(true)
                .contentType(ContentType.OPUS.toString())
                //.model("en-UK_NarrowbandModel")
                .interimResults(true)
                .inactivityTimeout(5000)
                //TODO: Uncomment this to enable Speaker Diarization
                //.speakerLabels(true)
                .build();
    }

    private class MicrophoneRecognizeDelegate implements RecognizeCallback {

        @Override
        public void onTranscription(SpeechResults speechResults) {
            System.out.println(speechResults);
            //TODO: Uncomment this to enable Speaker Diarization
            /*recoTokens = new SpeakerLabelsDiarization.RecoTokens();
            if(speechResults.getSpeakerLabels() !=null)
            {
                recoTokens.add(speechResults);
                Log.i("SPEECHRESULTS",speechResults.getSpeakerLabels().get(0).toString());
            }*/
            if(speechResults.getResults() != null && !speechResults.getResults().isEmpty()) {
                String text = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
                if(text.toLowerCase().contains("burns")) {

                    showMicText(text);
                    Intent burn1  = new Intent(MainActivity.this, BurnsActivity.class);
                    startActivity(burn1);
                }
            }
        }

        @Override public void onConnected() {

        }

        @Override public void onError(Exception e) {
            showError(e);
            enableMicButton();
        }

        @Override public void onDisconnected() {
            enableMicButton();
        }

        @Override
        public void onInactivityTimeout(RuntimeException runtimeException) {

        }

        @Override
        public void onListening() {

        }

        //@Override
        public void onTranscriptionComplete() {

        }
    }

    private void showMicText(final String text) {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                inputMessage.setText(text);
            }
        });
    }

    private void enableMicButton() {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                search.setEnabled(true);
            }
        });
    }

    private void showError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

}







