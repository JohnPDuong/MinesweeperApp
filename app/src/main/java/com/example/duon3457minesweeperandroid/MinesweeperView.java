/**
 * MinesweeperView responsible for event handling and game display
 *
 * @author John Duong
 */
package com.example.duon3457minesweeperandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.media.MediaPlayer;
import android.view.MotionEvent;

import android.view.View;

import androidx.appcompat.app.AlertDialog;

import duon3457Minesweeper.Board;
import duon3457Minesweeper.MinesweeperPresenter;

import static duon3457Minesweeper.MinesweeperModel.EMPTY;
import static duon3457Minesweeper.MinesweeperModel.MINE;
import static duon3457Minesweeper.MinesweeperModel.UNREVEALED;

/**
 * Interface for minesweeper view class
 *
 * @author John Duong
 */
public class MinesweeperView extends View
{
  private final int RECTANGLES_PER_ROW = 9;
  private final int RECTANGLES_PER_COL = 9;

  private Paint mRevealed = new Paint ();
  private Paint mHidden = new Paint ();
  private Paint mBackground = new Paint ();
  private Paint mMine = new Paint ();
  private Paint mText = new Paint ();

  private float mTextX;
  private float mTextY;

  private final boolean NO_RESTART = false;
  private final boolean RESTART = true;

  private int mRectangleHeight;
  private int mRectangleWidth;

  private int mDifficulty;
  private int mSeed;

  private boolean mbNeedsRestart;

  private MainActivity mMainActivity;

  private MinesweeperPresenter mPresenter;

  private Rect mSelectedRectangle = new Rect ();

  private final MediaPlayer mMP = MediaPlayer.create (getContext (), R.raw.lose);

  /**
   * Constructor for MinesweeperView, initializes font, difficulty, seed, colors and mPresenter
   *
   * @param context    the context
   * @param difficulty the game difficulty
   * @param seed       the seed for random number generator
   */
  public MinesweeperView (Context context, int difficulty, int seed)
  {
    super (context);

    mText.setTextSize (context.getResources ().getDisplayMetrics ().heightPixels /
                       RECTANGLES_PER_COL);

    mDifficulty = difficulty;
    mSeed = seed;

    setFocusable (true);
    setFocusableInTouchMode (true);

    this.mMainActivity = (MainActivity) context;

    mHidden.setColor (Color.CYAN);
    mRevealed.setColor (Color.YELLOW);
    mBackground.setColor (Color.BLACK);
    mMine.setColor (Color.RED);

    mText.setColor (Color.BLACK);

    mbNeedsRestart = NO_RESTART;
    mPresenter = new MinesweeperPresenter (RECTANGLES_PER_ROW, RECTANGLES_PER_COL,
                                          mDifficulty, mSeed);
  }
  /**
   * Draws the grid, cells and displays the number. Also checks for lose and win
   *
   * @param canvas the canvas
   */
  protected void onDraw (Canvas canvas)
  {
    Board display = mPresenter.getBoard ();
    Rect rectangle = new Rect ();
    Paint rectPaint = new Paint ();

    int cellType;

    mRectangleHeight = getHeight () / display.getLength ();
    mRectangleWidth = getWidth () / display.getWidth ();

    canvas.drawRect (0, 0, getWidth (), getHeight (), mBackground);

    for (int i = 0; i < display.getLength (); i++)
    {
      for (int j = 0; j < display.getWidth (); j++)
      {
        rectangle.set ( (i * mRectangleWidth),
                 (j * mRectangleHeight),
                 (i * mRectangleWidth + mRectangleWidth - 1),
                 (j * mRectangleHeight + mRectangleHeight - 1));

        mTextX = rectangle.exactCenterX () - mRectangleWidth / 2.5f;
        mTextY = rectangle.exactCenterY () + mRectangleHeight / 2.5f;

        cellType = display.getCellVal (j, i);

        switch (cellType)
        {
          case UNREVEALED:
            rectPaint = mHidden;
            break;
          case MINE:
            rectPaint = mMine;
            break;
          default:
            rectPaint = mRevealed;
            break;
        }
        canvas.drawRect (rectangle, rectPaint);

        if (EMPTY != cellType && UNREVEALED != cellType && MINE != cellType)
        {
          canvas.drawText (Integer.toString (cellType), mTextX, mTextY, mText);
        }
      }
    }

    if (!mPresenter.continueGame ())
    {
      mbNeedsRestart = RESTART;

      if (mPresenter.result ())
      {
        onWin ();
      }
      else if (!mPresenter.result ())
      {
        onLose ();
      }
    }

    for (int i = 0; i < display.getLength (); i++)
    {
      canvas.drawLine (i * mRectangleWidth, 0, i * mRectangleWidth, getHeight (), mBackground);
    }
    for (int i = 1; i < display.getWidth (); i++)
    {
      canvas.drawLine (0, i * mRectangleHeight, getWidth (), i * mRectangleHeight, mBackground);
    }
  }

  /**
   * plays a sound when user loses game
   */
  private void onLose ()
  {
    mMP.start ();
  }

  /**
   * When a touch event happens, handles event, selects rectangle, exits dialog or
   * nothing happens
   *
   * @param event the event
   */
  @Override
  public boolean onTouchEvent (MotionEvent event)
  {
    if (event.getAction () != MotionEvent.ACTION_DOWN)
    {
      return super.onTouchEvent (event);
    }

    if (mbNeedsRestart)
    {
      mMainActivity.newGame ();
      mbNeedsRestart = NO_RESTART;
    }

    selectRectangle ( (int) (event.getX ()), (int) (event.getY ()));

    return true;
  }
  /**
   * Sets the selected rectangle
   *
   * @param xCoordinate the x coordinate
   * @param yCoordinate the y coordinate
   * @param rectangle   the rectangle object containing coordinates
   */
  private void setSelectedRectangle (int xCoordinate, int yCoordinate,
                                     Rect rectangle)
  {
    rectangle.set ( (xCoordinate * mRectangleWidth),
             (yCoordinate * mRectangleHeight),
             (xCoordinate * mRectangleWidth + mRectangleWidth - 1),
             (yCoordinate * mRectangleHeight + mRectangleHeight - 1));
  }
  /**
   * Converts xCoord and yCoord to grid coordinates
   *
   * @param xCoord x coordinate
   * @param yCoord y coordinate
   */
  private void selectRectangle (int xCoord, int yCoord)
  {
    int mXCoordSelectedRect;
    int mYCoordSelectedRect;

    invalidate (mSelectedRectangle);
    mXCoordSelectedRect = xCoord / mRectangleWidth;
    mYCoordSelectedRect = yCoord / mRectangleHeight;
    setSelectedRectangle (mXCoordSelectedRect, mYCoordSelectedRect, mSelectedRectangle);
    mPresenter.checkCell (mYCoordSelectedRect, mXCoordSelectedRect);
    invalidate (mSelectedRectangle);
  }

  /**
   * When the user wins, display a dialog. Pressing continue will start a new game.
   */
  public void onWin ()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder (getContext ());
    builder.setTitle ("Winner!");
    builder.setMessage ("Congratulations, you won the game");

    builder.setPositiveButton ("Continue", new DialogInterface.OnClickListener ()
    {
      @Override
      public void onClick (DialogInterface dialog, int id)
      {
        switch (id)
        {
          case DialogInterface.BUTTON_POSITIVE:
            mMainActivity.newGame ();
            break;
          default:
            break;
        }
      }
    });

    AlertDialog dialog = builder.create ();
    dialog.show ();
  }
}