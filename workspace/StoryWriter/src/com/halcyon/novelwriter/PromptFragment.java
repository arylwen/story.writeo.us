package com.halcyon.novelwriter;

import android.os.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.halcyon.storywriter.*;

public class PromptFragment extends SherlockFragment
 {

        public static final String ARG_PROMPT = "prompt";
		
		private EditText prompt;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_prompt, container, false);
			
			ScrollView scroll = (ScrollView) rootView.findViewById(R.id.promptscroll);	
			if (scroll != null)
			{
				scroll.setFillViewport(true);
				scroll.setHorizontalScrollBarEnabled(false);
			}	
			
            Bundle args = getArguments();
			prompt =   ((EditText) rootView.findViewById(R.id.prompt));
            prompt.setText(args.getString((ARG_PROMPT)));
            return rootView;
        }
		
		public String getPrompt(){
			String ret = null;
			if(prompt != null){
				ret = prompt.getText().toString();
			}
			return ret;
		}
    }
