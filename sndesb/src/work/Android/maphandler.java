package work.Android;

import java.io.Serializable;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
//import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.Overlay;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
//import android.view.View;
//import android.widget.LinearLayout;


public class maphandler extends MapActivity 
{    
    MapView mapView; 
    MapController mc;
    GeoPoint p;
    String coordinates[];
      
    class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, 
        boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(p, screenPts);
 
            //---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(
                getResources(), R.drawable.tavlpos);            
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
            return true;
        }
    } 
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;		
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maphandler);

        coordinates = new String[2];
		Serializable s = this.getIntent().getSerializableExtra("arguments");
		Object[] o = (Object[]) s;

		if (o != null) {
   			Log.i("XTRACTOR","maphandler : onCreate : Y/X "+ o[1].toString() + o[3].toString());
			coordinates[0] = (o[1].toString());
			coordinates[1] = (o[3].toString());
		}
		
        mapView = (MapView) findViewById(R.id.mapView);
//        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);  

        mapView.setBuiltInZoomControls(true);
        
/*        View zoomView = mapView.getZoomControls(); 
 
        zoomLayout.addView(zoomView, 
            new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT)); 
        mapView.displayZoomControls(true);
*/ 
        mc = mapView.getController();
//        String coordinates[] = {"1.352566007", "103.78921587"};
//         String coordinates[] = {"59.22928", "18.48135"};
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);

//	Ingarš        
//        	59°13'45.39"N    18°28'52.88"O
//        	59 + 13/60 + 45.39/3600 = 0.216666667 + 0.012608333 = 59.22928 
//			18 + 28/60 + 52.88/3600 = 0.466666667 + 0.014444444 = 18.48135
        //        58.21696 12.29065
        // 59+(17/60)+(23/3600) = 59,2897
        
        
        p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6));
 
        mc.animateTo(p);
        mc.setZoom(17); 
        
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);              
        
        mapView.invalidate();        
    }
 
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}

