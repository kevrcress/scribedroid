package tyler.breisacher.scribe;

import tyler.breisacher.scribe.model.MiniGrid;
import tyler.breisacher.scribe.model.XY;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        MiniGrid miniGrid = MiniGrid.fromString("+O+\nOO+\n++O");
        
        TableLayout miniGridView = (TableLayout) findViewById(R.id.miniGrid);
        
        for (XY xy : XY.allXYs()) {
          TableRow row = (TableRow) miniGridView.getChildAt(xy.y);
          String text = miniGrid.get(xy).toChar() + "";
          ((TextView) row.getChildAt(xy.x)).setText(text);
        }
    }
}