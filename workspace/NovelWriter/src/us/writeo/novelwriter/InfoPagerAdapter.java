package us.writeo.novelwriter;

import android.os.*;
import android.support.v4.app.*;
import android.text.*;
import android.util.*;
import android.view.*;
import java.util.*;


public class InfoPagerAdapter extends FragmentStatePagerAdapter
{
	private static final String TAG = "NW InfoPagerAdapter";
	private String prompt;
	private String templateName;
	//private String novelFileName;
	private SpannableString templateSummary;
	private SpannableString wordCounterList;

	private PromptFragment promptFragment;
	private TemplateInfoFragment templateInfoFragment;
	private WordCountersFragment wordCountersFragment;
	private FragmentManager fragmentManager;

	public InfoPagerAdapter(FragmentManager fm, String aPrompt)
	{
		super(fm);
		prompt = aPrompt;
		fragmentManager = fm;
	}

	@Override
	public Fragment getItem(int i)
	{
		Log.e(TAG, "getItem called " + i);
		Fragment fragment = null;
		Bundle args;

		switch (i)
		{

			case 0:
				fragment = new PromptFragment();
				promptFragment = (PromptFragment)fragment;
				Log.e(TAG, "We have a prompt fragment");
				args = new Bundle();
				args.putString(PromptFragment.ARG_PROMPT, prompt); 
				fragment.setArguments(args);
			    break;

			case 1:
				fragment = new TemplateInfoFragment();
				templateInfoFragment = (TemplateInfoFragment)fragment;
				args = new Bundle();
				args.putString(TemplateInfoFragment.ARG_TEMPLATE_NAME, templateName); 
				//spannable is not serializable so it cannot be sent as an argument
				((TemplateInfoFragment)fragment).setTemplateSummary(templateName, templateSummary);
				fragment.setArguments(args);
				break;

			case 2:
				fragment = new WordCountersFragment();
				wordCountersFragment = (WordCountersFragment)fragment;
				args = new Bundle();
				args.putString(WordCountersFragment.ARG_COUNTER_NAME, templateName); 
				//spannable is not serializable so it cannot be sent as an argument
				((WordCountersFragment)fragment).setWordCounters(templateName, wordCounterList);
				fragment.setArguments(args);
				break;


			default:

		}

		return fragment;
	}

	@Override
	public int getCount()
	{
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		String[] titles = {"Prompt", "Template Summary", "Word Counters"};
		return titles[position];
	}

	public void setTemplate(String aName, SpannableString aSummary)
	{
		templateName = aName;
		templateSummary = aSummary;
		if (templateInfoFragment != null){
			templateInfoFragment.setTemplateSummary(templateName, templateSummary);
		}
	}

	public void setWordCounters(String aName, SpannableString aWordCounterList)
	{
		templateName = aName;
		wordCounterList = aWordCounterList;
		if(wordCountersFragment != null){
			wordCountersFragment.setWordCounters(aName, aWordCounterList);
		}
	}

	public String getPrompt()
	{
		return promptFragment.getPrompt();
	}

	//undo/redo related functions
	public boolean canUndo()
	{
		return promptFragment.canUndo();
	}

	public boolean canRedo()
	{
		return promptFragment.canRedo();
	}

	public void undo()
	{
		promptFragment.undo();
	}
	
	public void redo()
	{
		promptFragment.redo();
	}

	public void destroyItem(ViewGroup container, int position, Object object)
	{
		Log.e(TAG, "destroyed object " + object);
		super.destroyItem(container, position, object);
	}

	public Object instantiateItem(ViewGroup container, int position)
	{
		Object object = super.instantiateItem(container, position);
		Log.e(TAG, "instantiated item " + object + " position " + position);
		if (position == 0)
		{
			promptFragment = (PromptFragment)object;
		}

		return object;
	}

}
