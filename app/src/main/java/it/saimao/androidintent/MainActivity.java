package it.saimao.androidintent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvMain;
    private ImageView ivMain;
    private Button btExplicit, btExplicitForResult, btImplicit, btImplicitForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
    }

    private final static int REQUEST_CODE = 123;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            if (activityResult.getResultCode() == RESULT_OK && activityResult.getData() != null) {
                Uri imageUrl = activityResult.getData().getData();
                ivMain.setImageURI(imageUrl);
            } else {
                Toast.makeText(MainActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        }
    });


    private void initUi() {
        ivMain = findViewById(R.id.imageView);
        tvMain = findViewById(R.id.tv_main);
        btExplicit = findViewById(R.id.bt_explicit);
        btExplicit.setOnClickListener(v -> {
            Intent explicitIntent = new Intent(this, HomeActivity.class);
            startActivity(explicitIntent);
        });
        btExplicitForResult = findViewById(R.id.bt_explicit_for_result);
        btExplicitForResult.setOnClickListener(v -> {
            Intent explicitForResult = new Intent(this, EditorActivity.class);
            startActivityForResult(explicitForResult, REQUEST_CODE);
        });

        btImplicit = findViewById(R.id.bt_implicit);
        btImplicit.setOnClickListener(v -> {
            Intent implicitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
            startActivity(implicitIntent);
        });

        btImplicitForResult = findViewById(R.id.bt_implicit_for_result);
        btImplicitForResult.setOnClickListener(v -> {
            Intent implicitForResult = new Intent(Intent.ACTION_PICK);
            implicitForResult.setType("image/*");
            launcher.launch(implicitForResult);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String text = data.getStringExtra("text");
                tvMain.setText(text);
            }
        }
    }

}