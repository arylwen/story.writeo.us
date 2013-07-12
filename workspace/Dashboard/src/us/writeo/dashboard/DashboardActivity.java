package us.writeo.dashboard;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;

import android.view.View.OnTouchListener;

public class DashboardActivity extends Activity implements OnItemClickListener {
  static final String EXTRA_MAP = "map";
  static final LauncherIcon[] ICONS = {            
      new LauncherIcon(R.drawable.story_writer, "Story Writer", 1),            
	  new LauncherIcon(R.drawable.novel_writer, "Novel Writer", 2),            
	  new LauncherIcon(R.drawable.character_generator, "Character Generator", 3),            
	  new LauncherIcon(R.drawable.novel_generator, "Novel Generator", 4),    
  };     
  
  @Override      
  public void onCreate(Bundle savedInstanceState) {        
      super.onCreate(savedInstanceState);        
      setContentView(R.layout.dashboard);         
      GridView gridview = (GridView) findViewById(R.id.dashboard_grid);        
      gridview.setAdapter(new ImageAdapter(this));        
      gridview.setOnItemClickListener(this);    
	  
      // Hack to disable GridView scrolling        
      gridview.setOnTouchListener(new OnTouchListener() {            
          @Override            
          public boolean onTouch(View v, MotionEvent event) {                
              return event.getAction() == MotionEvent.ACTION_MOVE;            
          }       
	   });    
  }     
  
  	private boolean intentExists(String aintent){
		boolean ret = false;
		Intent intent =  new Intent(aintent);
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
		ret = activities.size() > 0;
		return ret;
	}
  
  @Override    
  public void onItemClick(AdapterView<?> parent, View v, int position, long id) {    
      int menuId = ICONS[position].menuId;
	  switch(menuId){
		  case 1:
		      String swintent = "us.writeo.storywriter";
			  if(intentExists(swintent) ){
		          Intent intent = new Intent(swintent);
			      startActivity(intent);
			  } else {
				  //display dialog and send the user to the appstore
			  }
			  break;
			  
		  case 2:
		      String nwintent = "us.writeo.novelwriter";
			  if(intentExists(nwintent) ){
		          Intent intent = new Intent(nwintent);
			      startActivity(intent);
			  } else {
				  //display dialog and send the user to the appstore
			  }
		  break;
		  
		  default:
		  break;
	  }
  }     
  
  static class LauncherIcon {        
      final String text;        
      final int imgId;        
      final int menuId;         
  
      public LauncherIcon(int imgId, String text, int menuId) {            
          super();            
          this.imgId = imgId;            
          this.text = text;            
          this.menuId= menuId;        
	  }     
   }     
  
  static class ImageAdapter extends BaseAdapter {        
  private Context mContext;         
  public ImageAdapter(Context c) {            
  mContext = c;        }         
  
  @Override        
  public int getCount() {            
  return ICONS.length;        }         
  
  @Override        
  public LauncherIcon getItem(int position) {            
  return null;        }         
  
  @Override        
  public long getItemId(int position) {            
  return 0;        }         
  
  static class ViewHolder {            
  public ImageView icon;           
  public TextView text;        }         
  
  // Create a new ImageView for each item referenced by the Adapter        
  @Override        
  public View getView(int position, View convertView, ViewGroup parent) {            
  View v = convertView;            
  ViewHolder holder;            
  if (v == null) {                
  LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
  v = vi.inflate(R.layout.dashboard_icon, null);                
  holder = new ViewHolder();                
  holder.text = (TextView) v.findViewById(R.id.dashboard_icon_text);                
  holder.icon = (ImageView) v.findViewById(R.id.dashboard_icon_img);                
  v.setTag(holder);            
  } else {                
  holder = (ViewHolder) v.getTag();            }             
  
  holder.icon.setImageResource(ICONS[position].imgId);            
  holder.text.setText(ICONS[position].text);             
  return v;        }    
  }
}
