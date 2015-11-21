package easypermissions.yashodhandivakaran.com.demo;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import easypermissions.yashodhandivakaran.com.libray.EasyPermissions;
import easypermissions.yashodhandivakaran.com.libray.EasyPermissionCallback;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback,
        EasyPermissionCallback {

    private View rootView;
    private EasyPermissions mPermissions;
    private Button storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.coordinator_layout);
        storage = (Button)findViewById(R.id.storage_button);
        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForStoragePermission();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Request permission with basic settings
     */
    private void requestForStoragePermission(){
        mPermissions = new EasyPermissions.Builder(this,rootView,this).build();
        mPermissions.setPermissionDeniedMessage(EasyPermissions.PERMISSION_REQUEST_STORAGE, "Permission has been denied");

        Bundle bundle = new Bundle();
        bundle.putString("INFO","Dummy info provided");
        mPermissions.setBundleForRequest(EasyPermissions.PERMISSION_REQUEST_STORAGE,bundle);


        mPermissions.grantPermission(EasyPermissions.PERMISSION_REQUEST_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "Dummy Rational Mesage",
                "Requesting for storage permission");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mPermissions != null){
            mPermissions.processResult(requestCode,grantResults);
        }

    }

    @Override
    public void permissionGrantedContinueTask(int requestCode, Bundle bundle) {
        //based on requestCode user can take actions

        //if any bundle is set while requesting the permission then that bundle information is provide inside this
        Toast.makeText(this,bundle.getString("INFO"),Toast.LENGTH_LONG).show();

    }

    @Override
    public void permissionDeniedContinueTask(int requestCode, Bundle bundle) {

    }

    @Override
    public void processBeforePermissionRequested(int requestCode, Bundle bundle) {

    }

    @Override
    public void processBeforePermissionRationaleRequested(int requestCode, Bundle bundle) {

    }

    @Override
    public void permissionAlreadyGrantedContinueTask(int requestCode, Bundle bundle) {

    }
}
