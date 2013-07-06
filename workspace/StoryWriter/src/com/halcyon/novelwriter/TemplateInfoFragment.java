package com.halcyon.novelwriter;

import android.os.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.halcyon.storywriter.*;
import android.text.*;

	public class TemplateInfoFragment extends SherlockFragment
	{

        public static final String ARG_TEMPLATE_NAME = "templateName";
	    public static final String ARG_TEMPLATE_SUMMARY = "templateSummary";

		private TextView templateName;
		private TextView templateSummary;
		private SpannableString templateSummaryStr;
		private String templateNameStr;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_template_info, container, false);

            Bundle args = getArguments();
			templateName =   ((TextView) rootView.findViewById(R.id.template_name));
            templateName.setText(args.getString((ARG_TEMPLATE_NAME)));
			
			templateSummary =   ((TextView) rootView.findViewById(R.id.template_summary));
			if(templateSummaryStr != null){
                templateSummary.setText(templateSummaryStr);
			}
			
            return rootView;
        }
		
		public void setTemplateSummary(String aName, SpannableString aTemplateSummary){
		    templateNameStr = aName;
			if(templateName != null){
				templateName.setText(aName);
			}
			templateSummaryStr = aTemplateSummary;
			if(templateSummary != null){
				templateSummary.setText(templateSummaryStr);
			}
		}
    }
