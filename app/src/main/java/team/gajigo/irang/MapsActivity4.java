package team.gajigo.irang;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

public class MapsActivity4 extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private DBAdapter dbAdapter;    //추가
    // 권한 체크 요청 코드 정의
    public static final int REQUEST_CODE_PERMISSIONS = 1000;
    // GoogleMap 실행 정의
    private GoogleApiClient mGoogleApiClient;
    // 위치 정보 얻는 객체
    private FusedLocationProviderClient mFusedLocationClient;
    private ClusterManager<MyItem> mClusterManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#ff6e63"));
        }
        setContentView(R.layout.activity_maps);

        this.dbAdapter = new DBAdapter(this);       //추가
        dbAdapter.reset();

        // GoogleAPIClient의 인스턴스 생성
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng seoul = new LatLng(37.566508, 126.977924);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

        this.dbAdapter = new DBAdapter(this);
        copyExcelDataToDatabase();

        // 권한 체크
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSIONS);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mClusterManager = new ClusterManager<MyItem>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem myItem) {
                Toast.makeText(MapsActivity4.this, myItem.getTitle() + "\n" + myItem.getPosition().toString(),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        addItems();

    }


    //GPS 권한설정
    @Override
    public void onRequestPermissionsResult
    (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                if (ActivityCompat.checkSelfPermission
                        (this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission
                        (this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한 체크 거부 됨", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void addItems() {

        Intent intent = getIntent();

        dbAdapter.open();
//        dbAdapter.deleteAll();
        Cursor result = dbAdapter.fetchAllNotes(intent.getStringExtra("category"));
        result.moveToFirst();
        String resultStr = "";
        while (!result.isAfterLast()) {
            Integer rowId = result.getInt(0);
            String tno = result.getString(1);
            String tname = result.getString(2);
            String sno = result.getString(3);
            String sname = result.getString(4);
            String saddress = result.getString(5);
            String stel = result.getString(6);
            String scatg = result.getString(7);
            String slat = result.getString(8);
            String slong = result.getString(9);

            MyItem offsetItem = new MyItem(Double.parseDouble(slat), Double.parseDouble(slong), sname);
            mClusterManager.addItem(offsetItem);
            result.moveToNext();
        }

        result.close();
        dbAdapter.close();
    }


    private void copyExcelDataToDatabase() {
        Log.w("ExcelToDatabase", "copyExcelDataToDatabase()");

        Workbook workbook = null;
        Sheet sheet = null;

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("notes.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {
                sheet = workbook.getSheet(3);

                if (sheet != null) {

                    int nMaxColumn = 9;
                    int nRowStartIndex = 0;
                    int nRowEndIndex = sheet.getColumn(nMaxColumn - 1).length - 1;
                    int nColumnStartIndex = 0;
                    int nColumnEndIndex = sheet.getRow(9).length - 1;

                    dbAdapter.open();
                    for (int nRow = nRowStartIndex; nRow <= nRowEndIndex; nRow++) {
                        String tno = sheet.getCell(nColumnStartIndex, nRow).getContents();
                        String tname = sheet.getCell(nColumnStartIndex + 1, nRow).getContents();
                        String sno = sheet.getCell(nColumnStartIndex + 2, nRow).getContents();
                        String sname = sheet.getCell(nColumnStartIndex + 3, nRow).getContents();
                        String saddress = sheet.getCell(nColumnStartIndex + 4, nRow).getContents();
                        String stel = sheet.getCell(nColumnStartIndex + 5, nRow).getContents();
                        String scatg = sheet.getCell(nColumnStartIndex + 6, nRow).getContents();
                        String slat = sheet.getCell(nColumnStartIndex + 7, nRow).getContents();
                        String slong = sheet.getCell(nColumnStartIndex + 8, nRow).getContents();

                        dbAdapter.createNote(tno, tname, sno, sname, saddress, stel, scatg,
                                slat, slong);
                    }
                    dbAdapter.close();
                } else {
                    System.out.println("Sheet is null!!");
                }
            } else {
                System.out.println("WorkBook is null!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

}
