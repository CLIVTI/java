/*
 * Copyright 2018 Gunnar Flötteröd
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * contact: gunnar.flotterod@gmail.com
 *
 */ 
package stockholm.ihop2.integration;

import java.util.Collection;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;

/**
 * 
 * @author Gunnar Flötteröd
 *
 */
public class PlansAnalyzer {

	private int home_cnt = 0;

	private int home_work_home_cnt = 0;

	private int home_other_home_cnt = 0;

	private int home_work_home_other_home_cnt = 0;

	// double to avoid casting
	private double total_cnt = 0;

	public PlansAnalyzer() {
	}

	public void process(Collection<Id<Person>> consideredIDs,
			final Population population) {
		for (Id<Person> id : consideredIDs) {
			final Plan plan = population.getPersons().get(id).getSelectedPlan();
			if (plan.getPlanElements().size() == 1) {
				this.home_cnt++;
			} else if (plan.getPlanElements().size() == 5) {
				if (((Activity) plan.getPlanElements().get(2)).getType()
						.toUpperCase().startsWith("W")
				// "work".equals(((Activity) plan.getPlanElements().get(2))
				// .getType())
				) {
					this.home_work_home_cnt++;
				} else if (((Activity) plan.getPlanElements().get(2)).getType()
						.toUpperCase().startsWith("O")
				// "other".equals(((Activity) plan.getPlanElements()
				// .get(2)).getType())
				) {
					this.home_other_home_cnt++;
				} else {
					throw new RuntimeException("problem with agent " + id);
				}
			} else if (plan.getPlanElements().size() == 9) {
				this.home_work_home_other_home_cnt++;
			} else {
				throw new RuntimeException("Plan of person " + id + " has "
						+ plan.getPlanElements().size() + " elements.");
			}
		}
		this.total_cnt += consideredIDs.size();
	}

	public String getReport() {
		final StringBuffer result = new StringBuffer();

		result.append("type\tabsolute\trelative\n");

		result.append("h\t");
		result.append(this.home_cnt);
		result.append("\t");
		result.append(this.home_cnt / this.total_cnt);
		result.append("\n");

		result.append("hwh\t");
		result.append(this.home_work_home_cnt);
		result.append("\t");
		result.append(this.home_work_home_cnt / this.total_cnt);
		result.append("\n");

		result.append("hoh\t");
		result.append(this.home_other_home_cnt);
		result.append("\t");
		result.append(this.home_other_home_cnt / this.total_cnt);
		result.append("\n");

		result.append("hwhoh\t");
		result.append(this.home_work_home_other_home_cnt);
		result.append("\t");
		result.append(this.home_work_home_other_home_cnt / this.total_cnt);
		result.append("\n");

		return result.toString();
	}

}
