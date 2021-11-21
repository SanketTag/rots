package com.rots.contract;

import org.apache.commons.collections4.Predicate;
import com.rots.entity.RotsMachineDataDetails;
import com.rots.entity.RotsTargetDetails;

public class ShiftNumberPredicate implements Predicate {	

		  /** The value to compare to */
		  private Integer shiftId;

		  public ShiftNumberPredicate(Integer shiftId) {
		    super();
		    this.shiftId = shiftId;
		  }

		  /**
		   * returns true when the salary is >= iValue
		   */
		  public boolean evaluate(Object object) {
		    if (object instanceof RotsTargetDetails) {     
		        return ((RotsTargetDetails) object).getShiftId().equals(this.shiftId);
		   
		  }else{
			  return false;
		  }
		    
		 }

}
