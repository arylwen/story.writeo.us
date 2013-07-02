package com.halcyon.novelwriter;

import android.os.*;
import android.support.v4.app.*;
import android.text.*;
import android.util.*;

public class InfoPagerAdapter extends FragmentStatePagerAdapter
 {
	    private static final String TAG = "NW InfoPagerAdapter";
	    private String prompt;
		private String templateName;
		private SpannableString templateSummary;
		private PromptFragment promptFragment;

        public InfoPagerAdapter(FragmentManager fm, String aPrompt){
		                //String aTemplateName, SpannableString aTemplateSummary) {
            super(fm);
			prompt = aPrompt;
			//templateName = aTemplateName;
			//templateSummary = aTemplateSummary;
        }

        @Override
        public Fragment getItem(int i) {
			Log.e(TAG, "getItem called "+i);
			Fragment fragment = null;
			Bundle args;
			
			switch(i){
				
				case 0:
                   fragment = new PromptFragment();
				   promptFragment = (PromptFragment)fragment;
				   Log.e(TAG, "We have a prompt fragment");
                   args = new Bundle();
                   args.putString(PromptFragment.ARG_PROMPT, prompt); // Our object is just an integer :-P
                   fragment.setArguments(args);
			    break;
				
				case 1:
					fragment = new TemplateInfoFragment();
					args = new Bundle();
					args.putString(TemplateInfoFragment.ARG_TEMPLATE_NAME, templateName); 
					//args.putSerializable(TemplateInfoFragment.ARG_TEMPLATE_SUMMARY, templateSummary);
					((TemplateInfoFragment)fragment).setTemplateSummary(templateName, templateSummary);
					fragment.setArguments(args);
					break;
				
				default:
				
			}
			
			
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
			String[] titles = {"Prompt", "Template Summary"};
            return titles[position];
        }
		
		public void setTemplate(String aName, SpannableString aSummary){
			templateName = aName;
			templateSummary = aSummary;
		}
		
		public String getPrompt(){
			return promptFragment.getPrompt();
		}
    }
