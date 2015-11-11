package com.games.bullscows;

import java.util.Vector;

import game.bullcow.BullCow;
import game.bullcow.Chislo;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		AlertDialog dlg = (AlertDialog) dialog;
		dlg.setMessage(getAnswerMessage());

		super.onPrepareDialog(id, dialog);
	}

	final private int digitNumber = 4;
	private AlertDialog alertDialog;
	private BullCow bc = new BullCow((byte) digitNumber);

	class SavedInfo {
		Vector<String> numbers;
		Vector<String> bulls;
		Vector<String> cows;
		BullCow engine;
		String textViewMessage;
		String buttonText;
		boolean buttonEnabled;
		State state;

		boolean isEmpty() {
			return engine == null || textViewMessage.isEmpty()
					|| buttonText.isEmpty();
		}

		boolean isTableEmpty() {
			return numbers == null || bulls == null || cows == null;
		}
	};

	private enum State {
		START, RUNNING, DONE
	};

	private State state = State.START;
	private byte lastBull;
	private byte lastCow;

	private int valueBull() {
		NumberPicker pickerBull = (NumberPicker) alertDialog
				.findViewById(R.id.numberPickerBull);
		return pickerBull.getValue();
	}

	private int valueCow() {
		NumberPicker pickerCow = (NumberPicker) alertDialog
				.findViewById(R.id.numberPickerCow);
		return pickerCow.getValue();
	}

	@SuppressWarnings("deprecation")
	private void checkOkButton() {
		int valueBull = valueBull();
		int valueCow = valueCow();

		if (valueBull >= 0 && valueCow >= 0
				&& (valueBull + valueCow <= digitNumber)) {
			alertDialog.getButton(Dialog.BUTTON1).setEnabled(true);
		} else {
			alertDialog.getButton(Dialog.BUTTON1).setEnabled(false);
		}
	}

	@SuppressWarnings("deprecation")
	public void resultOnClick(View view) {
		showDialog(0);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		SavedInfo data = new SavedInfo();
		data.engine = bc;
		data.state = state;
		Button button = (Button) findViewById(R.id.button_game);
		TextView view = (TextView) findViewById(R.id.textViewMessage);
		data.buttonEnabled = button.isEnabled();
		data.buttonText = (String) button.getText();
		data.textViewMessage = (String) view.getText();

		TableLayout layout = (TableLayout) findViewById(R.id.TableLayoutResults);
		int childCount = layout.getChildCount();
		if (childCount <= 1)
			return data;

		data.bulls = new Vector<String>();
		data.cows = new Vector<String>();
		data.numbers = new Vector<String>();
		for (int i = 1; i < childCount; i++) {
			TableRow lastRow = (TableRow) layout.getChildAt(i);

			if (lastRow.getChildCount() != 5)
				continue;

			TextView textNumber = (TextView) lastRow.getChildAt(0);
			TextView textBull = (TextView) lastRow.getChildAt(2);
			TextView textCow = (TextView) lastRow.getChildAt(4);
			data.numbers.add((String) textNumber.getText());
			data.cows.add((String) textCow.getText());
			data.bulls.add((String) textBull.getText());
		}

		return data;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogview = inflater.inflate(R.layout.choice_number, null);

		NumberPicker.OnValueChangeListener listener = new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker numberPicker, int i, int i1) {
				checkOkButton();
			}
		};

		NumberPicker picker = (NumberPicker) dialogview
				.findViewById(R.id.numberPickerBull);
		picker.setWrapSelectorWheel(false);
		picker.setMaxValue(digitNumber);
		picker.setMinValue(0);
		picker.setOnValueChangedListener(listener);

		picker = (NumberPicker) dialogview.findViewById(R.id.numberPickerCow);
		picker.setWrapSelectorWheel(false);
		picker.setMaxValue(digitNumber);
		picker.setMinValue(0);
		picker.setOnValueChangedListener(listener);

		AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(
				new ContextThemeWrapper(this, R.style.AlertDialogCustom));
		dialogbuilder.setTitle(R.string.answer);
		dialogbuilder.setView(dialogview);
		dialogbuilder.setIcon(R.drawable.ic_launcher);
		dialogbuilder.setMessage(getAnswerMessage());
		dialogbuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						changeLastRow(String.valueOf(valueBull()),
								String.valueOf(valueCow()));
						String msg = getString(R.string.bulls) + " "
								+ String.valueOf(valueBull()) + ", "
								+ getString(R.string.cows) + " "
								+ String.valueOf(valueCow());
						Toast.makeText(getApplicationContext(), msg,
								Toast.LENGTH_SHORT).show();
						enableButtonWithText(true, "");
					}
				});

		dialogbuilder.setNegativeButton("BACK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog = dialogbuilder.create();
		return alertDialog;
	}

	private void enableButtonWithText(boolean enabled, String text) {
		Button button = (Button) findViewById(R.id.button_game);
		button.setEnabled(enabled);
		if (text.isEmpty())
			return;
		button.setText(text);
	}

	private void setTextMessage(String text) {
		TextView view = (TextView) findViewById(R.id.textViewMessage);
		view.setText(text);
	}

	private void resetResultTable() {
		TableLayout layout = (TableLayout) findViewById(R.id.TableLayoutResults);

		int childCount = layout.getChildCount();
		if (childCount <= 1)
			return;

		layout.removeViews(1, childCount - 1);
	}

	private String getAnswerMessage() {
		return getString(R.string.answer_message) + " " + bc.getNumber() + " ?";
	}

	public void buttonGameOnClick(View v) {
		switch (state) {
		case START:
			state = State.RUNNING;
		case RUNNING:
			bc.answer(lastBull, lastCow);
			break;
		case DONE:
			bc.reset();
			resetResultTable();
			state = State.RUNNING;
			break;
		default:
			break;
		}

		if (bc.askNew()) {
			state = State.DONE;
			setTextMessage(getString(R.string.done) + " " + bc.getNumber());
			enableButtonWithText(true, getString(R.string.button_start_again));
			return;
		}

		addRow(bc.getNumber(), "???", "???");
		setTextMessage(getAnswerMessage());
		enableButtonWithText(false, getString(R.string.button_answer));
	}

	private void changeLastRow(String bull, String cow) {
		TableLayout layout = (TableLayout) findViewById(R.id.TableLayoutResults);

		int childCount = layout.getChildCount();
		if (childCount <= 0)
			return;

		TableRow lastRow = (TableRow) layout.getChildAt(childCount - 1);

		childCount = lastRow.getChildCount();
		if (childCount != 5)
			return;

		TextView textBull = (TextView) lastRow.getChildAt(2);
		TextView textCow = (TextView) lastRow.getChildAt(4);

		textBull.setText(bull);
		lastBull = (byte) Integer.parseInt(bull);
		lastCow = (byte) Integer.parseInt(cow);
		textCow.setText(cow);
	}

	private void addRow(String number, String bull, String cow) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View row = inflater.inflate(R.layout.table_row_results, null);
		TableLayout layout = (TableLayout) findViewById(R.id.TableLayoutResults);
		TextView textNumber = (TextView) row.findViewById(R.id.textViewNumber);
		TextView textBull = (TextView) row.findViewById(R.id.textViewMessage);
		TextView textCow = (TextView) row.findViewById(R.id.textViewCow);
		textNumber.setText(number);
		textBull.setText(bull);
		textCow.setText(cow);
		layout.addView(row, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		@SuppressWarnings("deprecation")
		final SavedInfo data = (SavedInfo) getLastNonConfigurationInstance();
		if (data != null && !data.isEmpty()) {
			bc = data.engine;
			enableButtonWithText(data.buttonEnabled, data.buttonText);
			setTextMessage(data.textViewMessage);
			state = data.state;

			if (!data.isTableEmpty()) {
				int count = data.numbers.size();
				for (int i = 0; i < count; i++) {
					addRow(data.numbers.elementAt(i), data.bulls.elementAt(i),
							data.cows.elementAt(i));
				}
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		Log.i("Tag", "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.i("Tag", "onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.i("Tag", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.i("Tag", "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.i("Tag", "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.i("Tag", "onStop");
		super.onStop();
	}
}
