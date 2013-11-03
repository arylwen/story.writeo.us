package us.writeo.novelwriter;

import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import us.writeo.*;
import com.actionbarsherlock.app.*;

	public class WordCountersFragment extends SherlockFragment
	{

        public static final String ARG_COUNTER_NAME = "countersName";
	    public static final String ARG_COUNTER_SUMMARY = "countersSummary";

		//private TextView templateName;
		private TextView countersSummary;
		private SpannableString countersSummaryStr;
		private String templateNameStr;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_word_counters, container, false);

            //Bundle args = getArguments();
			//templateName =   ((TextView) rootView.findViewById(R.id.template_name));
            //templateName.setText(args.getString((ARG_TEMPLATE_NAME)));

			countersSummary =   ((TextView) rootView.findViewById(R.id.counters_summary));
			if(countersSummaryStr != null){
                countersSummary.setText(countersSummaryStr);
			}

            return rootView;
        }

		public void setWordCounters(String aName, SpannableString aWordCountersList){
		    templateNameStr = aName;
			//if(templateName != null){
			//	templateName.setText(aName);
			//}
			countersSummaryStr = aWordCountersList;
			if(countersSummary != null){
				countersSummary.setText(countersSummaryStr);
			}
		}
    }
