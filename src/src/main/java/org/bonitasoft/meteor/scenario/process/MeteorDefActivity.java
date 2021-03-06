package org.bonitasoft.meteor.scenario.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.log.event.BEvent;
import org.bonitasoft.log.event.BEvent.Level;
import org.bonitasoft.meteor.MeteorToolbox;

/**
 * describe a Human activity Inside a process. Then, this human activity can be
 * process by one or multiple robot.
 */

public class MeteorDefActivity extends MeteorDefBase {

	// attention, the processDefinitionID must be recalculated each time: it may
	// change
	// public Long mProcessDefinitionId;
	public Long mActivityDefinitionId;
	public String mActivityName;

	public MeteorDefProcess mMeteorProcess;

	public MeteorDefActivity(MeteorDefProcess meteorProcess, long activityId) {
		mMeteorProcess = meteorProcess;
		mActivityDefinitionId = activityId;
	}

	public String getHtmlId() {
		return MeteorMain.cstHtmlPrefixActivity + (mActivityDefinitionId == null ? "#" : mActivityDefinitionId.toString());
	}

	public String getInformation() {
		return mActivityName;
	}

	public MeteorDefProcess getMeteorProcess() {
		return mMeteorProcess;
	}

	public static MeteorDefActivity getInstanceFromMap(MeteorDefProcess meteorProcess, final Map<String, Object> oneProcess, ProcessAPI processAPI) {

		// attention : the processdefinitionId is very long it has to be set
		// in STRING else JSON will do an error
		Long activityDefinitionId = MeteorToolbox.getParameterLong(oneProcess, MeteorMain.cstHtmlId, -1);
		MeteorDefActivity meteorActivity = new MeteorDefActivity(meteorProcess, activityDefinitionId.longValue());

		// mProcessDefinitionId = MeteorToolbox.getParameterLong(oneProcess,
		// MeteorProcessDefinitionList.cstHtmlProcessDefId, -1);
		// mProcessName = MeteorToolbox.getParameterString(oneProcess,
		// MeteorProcessDefinitionList.cstHtmlProcessName, "");
		// mProcessVersion = MeteorToolbox.getParameterString(oneProcess,
		// MeteorProcessDefinitionList.cstHtmlProcessVersion, "");
		meteorActivity.mActivityName = MeteorToolbox.getParameterString(oneProcess, MeteorMain.cstHtmlActivityName, "");
		
		
		meteorActivity.decodeFromMap(oneProcess, processAPI);
		return meteorActivity;
	}
	public void calculContractDefinition(ProcessAPI processAPI)
	{
		try {
			ContractDefinition contractDefinition = processAPI.getUserTaskContract(mActivityDefinitionId);
			setContractDefinition(contractDefinition);
		} catch (Exception e) {
			mListEvents.add(new BEvent(EventGetContract, e, "Task[" + mActivityName + "]"));
		}

	}		
	

	
	/**
	 * get the map for the activity
	 * @return
	 */
	public Map<String, Object> getMap() {
		final Map<String, Object> oneActivity = new HashMap<String, Object>();

		oneActivity.put(MeteorMain.cstHtmlActivityName, mActivityName);
	// attention, the activityId is very long it has to be
	// transform in STRING else JSON will mess it
	oneActivity.put(MeteorMain.cstHtmlId, mActivityDefinitionId.toString());
	oneActivity.put(MeteorMain.cstHtmlProcessDefId, mMeteorProcess.mProcessDefinitionId.toString());
	oneActivity.put(MeteorMain.cstJsonProcessName, mMeteorProcess.mProcessName);
	oneActivity.put(MeteorMain.cstHtmlProcessVersion, mMeteorProcess.mProcessVersion);
	fullfillMap( oneActivity );
	return oneActivity;
	}
}
