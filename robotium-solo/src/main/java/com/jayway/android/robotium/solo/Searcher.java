package com.jayway.android.robotium.solo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Instrumentation;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * This class contains various search methods. Examples are: searchEditText(),
 * searchText(), searchButton().
 * 
 * @author Renas Reda, renas.reda@jayway.com
 * 
 */

class Searcher {
	
	private final ViewFetcher soloView;
	private final Scroller soloScroll;
	private final Instrumentation inst;
	private final int PAUS = 500;
	private final int TIMEOUT = 5000;
	private int countMatches=0;
	private final String LOG_TAG = "Robotium";

    /**
     * Constructs this object.
     *
     * @param soloView the {@link ViewFetcher} instance.
     * @param soloScroll the {@link Scroller} instance.
     * @param inst the {@link Instrumentation} instance.
     */
    public Searcher(ViewFetcher soloView, Scroller soloScroll, Instrumentation inst) {
        this.soloView = soloView;
        this.soloScroll = soloScroll;
        this.inst = inst;
    }



	/**
	 * Searches for a text string in the edit texts located in the current
	 * activity.
	 *
	 * @param search the search string to be searched. Regular expressions are supported
	 * @return true if an edit text with the given text is found or false if it is not found
	 *
	 */
    
    public boolean searchEditText(String search) {
        long now = System.currentTimeMillis();
        final long endTime = now + TIMEOUT;
        while (!searchForEditText(search) && now < endTime)
        {
		RobotiumUtils.sleep(PAUS);        	
		now = System.currentTimeMillis();
        }
        return searchForEditText(search);
    }
	
    /**
	 * Searches for a text string in the edit texts located in the current
	 * activity.
	 *
	 * @param search the search string to be searched. Regular expressions are supported
	 * @return true if an edit text with the given text is found or false if it is not found
	 *
	 */
    
	public boolean searchForEditText(String search) {
		
		return searchForEditText(search, true);
	}
	
	
	 /**
	 * Searches for a text string in the edit texts located in the current
	 * activity.
	 *
	 * @param search the search string to be searched. Regular expressions are supported
	 * @param scroll set to true if scrolling should be performed
	 * @return true if an edit text with the given text is found or false if it is not found
	 *
	 */
	
	public boolean searchForEditText(String search, boolean scroll) {
		inst.waitForIdleSync();
		Pattern p = Pattern.compile(search);
		Matcher matcher;
		ArrayList<EditText> editTextList = soloView.getCurrentEditTexts();
		Iterator<EditText> iterator = editTextList.iterator();
		while (iterator.hasNext()) {
			EditText editText = (EditText) iterator.next();
			matcher = p.matcher(editText.getText().toString());
			if (matcher.find()) {
				return true;
			}
		}
		if (scroll && soloScroll.scrollDown())
			return searchForEditText(search, scroll);
		else
			return false;
	}
	
	
	/**
	 * Searches for a button with the given search string and returns true if at least one button 
	 * is found with the expected text
	 *
	 * @param search the string to be searched. Regular expressions are supported
	 * @return true if a button with the given text is found and false if it is not found
	 *
	 */
	
	public boolean searchButton(String search) {
		return searchButton(search, 0);
	}
	
	/**
	 * Searches for a toggle button with the given search string and returns true if at least one button 
	 * is found with the expected text
	 *
	 * @param search the string to be searched. Regular expressions are supported
	 * @return true if a toggle button with the given text is found and false if it is not found
	 *
	 */
	
	public boolean searchToggleButton(String search) {
		return searchToggleButton(search, 0);
	}
	
	/**
	 * Searches for a button with the given search string and returns true if the 
	 * searched button is found a given number of times
	 * 
	 * @param search the string to be searched. Regular expressions are supported
	 * @param matches the number of matches expected to be found. 0 matches means that one or more 
	 * matches are expected to be found
	 * @return true if a button with the given text is found a given number of times and false 
	 * if it is not found
	 *  
	 */
	
	public boolean searchButton(String search, int matches) {
		
        long now = System.currentTimeMillis();
        final long endTime = now + TIMEOUT;
        while (!searchForButton(search, matches) && now < endTime)
        {
        	now = System.currentTimeMillis();
        }
        return searchForButton(search, matches);
	}
	
	/**
	 * Searches for a button with the given search string and returns true if the 
	 * searched button is found a given number of times
	 * 
	 * @param search the string to be searched. Regular expressions are supported
	 * @param matches the number of matches expected to be found. 0 matches means that one or more 
	 * matches are expected to be found
	 * @return true if a button with the given text is found a given number of times and false 
	 * if it is not found
	 *  
	 */
	
	private boolean searchForButton(String search, int matches) {
		inst.waitForIdleSync();
		RobotiumUtils.sleep(PAUS);
		Pattern p = Pattern.compile(search);
		Matcher matcher;
		ArrayList<Button> buttonList = soloView.getCurrentButtons();
		Iterator<Button> iterator = buttonList.iterator();
		if(matches == 0)
			matches = 1;
		while (iterator.hasNext()) {
			Button button = (Button) iterator.next();
			matcher = p.matcher(button.getText().toString());
			if(matcher.find()){	
				countMatches++;
			}
			if (countMatches == matches) {
				countMatches = 0;
				return true;
			} 	
		}
		 if (soloScroll.scrollDown())
		{
			return searchForButton(search, matches);
		} else {
			Log.d(LOG_TAG, " There are only " + countMatches + " matches of " + search);
			return false;
		}

	}
	/**
	 * Searches for a toggle button with the given search string and returns true if the 
	 * searched toggle button is found a given number of times
	 * 
	 * @param search the string to be searched. Regular expressions are supported
	 * @param matches the number of matches expected to be found. 0 matches means that one or more 
	 * matches are expected to be found
	 * @return true if a toggle button with the given text is found a given number of times and false 
	 * if it is not found
	 *  
	 */
	
	public boolean searchToggleButton(String search, int matches) {
        long now = System.currentTimeMillis();
        final long endTime = now + TIMEOUT;
        while (!searchForToggleButton(search, matches) && now < endTime)
        {
        	now = System.currentTimeMillis();
        }
        return searchForToggleButton(search, matches);
	}
	
	
	/**
	 * Searches for a toggle button with the given search string and returns true if the 
	 * searched toggle button is found a given number of times
	 * 
	 * @param search the string to be searched. Regular expressions are supported
	 * @param matches the number of matches expected to be found. 0 matches means that one or more 
	 * matches are expected to be found
	 * @return true if a toggle button with the given text is found a given number of times and false 
	 * if it is not found
	 *  
	 */
	
	private boolean searchForToggleButton(String search, int matches) {
		inst.waitForIdleSync();
		RobotiumUtils.sleep(PAUS);
		Pattern p = Pattern.compile(search);
		Matcher matcher;
		ArrayList<ToggleButton> toggleButtonList = soloView.getCurrentToggleButtons();
		Iterator<ToggleButton> iterator = toggleButtonList.iterator();
		if(matches == 0)
			matches = 1;
		while (iterator.hasNext()) {
			ToggleButton toggleButton = (ToggleButton) iterator.next();
			matcher = p.matcher(toggleButton.getText().toString());
			if(matcher.find()){	
				countMatches++;
			}
			if (countMatches == matches) {
				countMatches=0;
				return true;
			} 
		}
		if (soloScroll.scrollDown())
		{
			return searchForToggleButton(search, matches);
		} else {
			if(countMatches > 0)
				Log.d(LOG_TAG, " There are only " + countMatches + " matches of " + search);
			return false;
		}

	}
	
	/**
	 * Searches for a text string and returns true if at least one item 
	 * is found with the expected text
	 *
	 * @param search the string to be searched. Regular expressions are supported
	 * @return true if the search string is found and false if it is not found
	 *
	 */
	
	public boolean searchText(String search) {
		return searchText(search, 0);
	}
	
	/**
	 * Searches for a text string and returns true if the searched text is found a given
	 * number of times
	 * 
	 * @param search the string to be searched. Regular expressions are supported
	 * @param matches the number of matches expected to be found. 0 matches means that one or more 
	 * matches are expected to be found
	 * @return true if search string is found a given number of times and false if the search string
	 * is not found
	 *  
	 */
	
	public boolean searchText(String search, int matches) {
		return searchText(search, matches, true);
	}
	
	/**
	 * Searches for a text string and returns true if the searched text is found a given
	 * number of times
	 * 
	 * @param search the string to be searched. Regular expressions are supported
	 * @param matches the number of matches expected to be found. 0 matches means that one or more 
	 * matches are expected to be found
	 * @return true if search string is found a given number of times and false if the search string
	 * is not found
	 *  
	 */
	
	public boolean searchText(String search, int matches, boolean scroll) {
        long now = System.currentTimeMillis();
        final long endTime = now + TIMEOUT;
        while (!searchForText(search, matches, scroll) && now < endTime)
        {
        	now = System.currentTimeMillis();
        }
        return searchForText(search, matches, scroll);
	}
	
	
	/**
	 * Searches for a text string and returns true if the searched text is found a given
	 * number of times
	 * 
	 * @param search the string to be searched. Regular expressions are supported
	 * @param matches the number of matches expected to be found. 0 matches means that one or more 
	 * matches are expected to be found
	 * @param scroll true if scrolling should be performed
	 * @return true if search string is found a given number of times and false if the search string
	 * is not found
	 *  
	 */
	
	public boolean searchForText(String search, int matches, boolean scroll) {
		inst.waitForIdleSync();
		RobotiumUtils.sleep(PAUS);
		Pattern p = Pattern.compile(search);
		Matcher matcher;
		ArrayList<TextView> textViewList = soloView.getCurrentTextViews(null);
		Iterator<TextView> iterator = textViewList.iterator();
		TextView textView = null;
		if(matches == 0)
			matches = 1;
		while (iterator.hasNext()) {
			textView = (TextView) iterator.next();
			matcher = p.matcher(textView.getText().toString());
			if(matcher.find()){	
				countMatches++;
			}
			if (countMatches == matches) {
				countMatches=0;
				return true;
			}
		}
		if (scroll && soloScroll.scrollDown()) {
			return searchForText(search, matches, scroll);
		} else {
			if (countMatches > 0)
				Log.d(LOG_TAG, " There are only " + countMatches + " matches of " + search);
			return false;
		}

	}
	

}
