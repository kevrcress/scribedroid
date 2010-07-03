package tyler.breisacher.scribe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tyler.breisacher.scribe.model.MiniGrid;
import tyler.breisacher.scribe.model.MiniGridListener;
import tyler.breisacher.scribe.model.ScribeMark;
import tyler.breisacher.scribe.model.XY;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MiniGridView extends TableLayout implements MiniGridListener, OnClickListener {
  private MiniGrid miniGrid;
  private int size;

  public MiniGridView(Context context, int size) {
    super(context);
    this.size = size;
    if (context instanceof Main) {
      this.setOnClickListener((Main) context);
    }
  }

  public MiniGrid getMiniGrid() {
    return this.miniGrid;
  }

  public void setMiniGrid(MiniGrid miniGrid) {
    this.miniGrid = miniGrid;
    rebuildLayout();
    miniGrid.addMiniGridListener(this);
  }

  private void rebuildLayout() {
    Log.i(Constants.LOG_TAG, this + ": rebuilding layout");
    this.removeAllViews();
    for (int y=0; y<3; y++) {
      TableRow row = new TableRow(this.getContext());
      this.addView(row);
      for (int x=0; x<3; x++) {
        CellView cell = new CellView(this.getContext(), this.size);
        cell.setXY(new XY(x, y));
        cell.setMark(this.miniGrid.get(x, y));
        if (size == Constants.MiniGridViewSize.LARGE)
          cell.setOnClickListener(this);
        row.addView(cell);
      }
    }
  }

  public CellView get(XY xy) {
    TableRow row = (TableRow) this.getChildAt(xy.y);
    return (CellView) row.getChildAt(xy.x);
  }

  private Iterable<CellView> allCellViews() {
    List<CellView> list = new ArrayList<CellView>(9);
    for (XY xy : XY.allXYs()) {
      list.add(this.get(xy));
    }
    return list;
  }

  /**
   * If this is a large (in-dialog) MiniGridView then each cell will have this
   * as its OnClickListener.
   */
  @Override
  public void onClick(View v) {
    XY xy = ((CellView) v).getXY();
    this.miniGrid.tryMove(xy);
  }

  @Override
  public void miniGridMarked(MiniGrid miniGrid, XY xy, ScribeMark mark) {
    Log.i(Constants.LOG_TAG, miniGrid + "[" + xy + "] marked with " + mark);
    if (miniGrid == this.miniGrid) {
      this.get(xy).setMark(mark);
    }
  }

  @Override
  public void miniGridEnabled(MiniGrid miniGrid, boolean enabled) {
    this.setEnabled(enabled);
  }

  @Override
  public void miniGridLastMovesChanged(MiniGrid miniGrid, Collection<XY> lastMoves) {
    for (CellView cellView : allCellViews()) {
      cellView.setLastMove(false);
    }
    for (XY xy : lastMoves) {
      this.get(xy).setLastMove(true);
    }
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    for (CellView cellView : allCellViews()) {
      cellView.setEnabled(enabled);
    }
  }
}
