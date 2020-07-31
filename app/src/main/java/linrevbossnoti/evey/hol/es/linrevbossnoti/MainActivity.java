package linrevbossnoti.evey.hol.es.linrevbossnoti;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    private EditText etLevel;
    private Button btnSave;
    private TextView tvField;
    private TextView tvElite;
    private NotificationReceiver mNotiReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotiReceiver = new NotificationReceiver();

        tvField = (TextView) findViewById(R.id.tv_field_monster);
        tvElite = (TextView) findViewById(R.id.tv_elite_monster);
        etLevel = (EditText) findViewById(R.id.et_level);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        refreshLevelView();


    }

    private void showLevelInputErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("1~260사이의 숫자를 입력하세요");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void refreshLevelView() {
        int savedLevel = Preferences.getInstance(this).getKeyPlayerLevel();
        etLevel.setText("" + savedLevel);
        tvField.setText(mNotiReceiver.getFieldMonsterStr(this));
        tvElite.setText(mNotiReceiver.getEliteMonsterStr(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                int level = Integer.parseInt(etLevel.getText().toString());
                if (level >= 1 && level <= 260) {
                    Preferences.getInstance(MainActivity.this).setKeyPlayerLevel(level);
                    new AlarmFactory(MainActivity.this).setAlarmOnReboot();
                    Toast.makeText(MainActivity.this, "이제 보스 및 사냥터 알림이 시작됩니다.", Toast.LENGTH_SHORT).show();
                    tvField.setText(mNotiReceiver.getFieldMonsterStr(this));
                    tvElite.setText(mNotiReceiver.getEliteMonsterStr(this));
                } else {
                    showLevelInputErrorDialog();
                }
                break;
        }
    }
}
