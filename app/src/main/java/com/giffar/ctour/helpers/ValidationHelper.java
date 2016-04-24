/**
 * @author egiadtya
 */

package com.giffar.ctour.helpers;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;

public abstract class ValidationHelper implements ValidationListener {
	private Context context;
	private Validator validator;
	public ValidationHelper(Context context,Object controller) {
		this.context = context;
		validator = new Validator(controller);
		validator.setValidationListener(this);
	}
	
	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		String message = failedRule.getFailureMessage();

		if (failedView instanceof EditText) {
			failedView.requestFocus();
			((EditText) failedView).setError(message);
		} else {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onValidationCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preValidation() {
		// TODO Auto-generated method stub
		
	}
	
	public Validator getValidator() {
		return validator;
	}
}
