package org.arquillian.reporter.impl.model.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.arquillian.reporter.api.utils.ReporterUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class DateChecker {

    public abstract String getDateFromReport();

    public void assertThatDateWasCorrectlyCreated() throws ParseException {
        Date before = new Date(System.currentTimeMillis());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String dateFromReport = getDateFromReport();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date after = new Date(System.currentTimeMillis());

        assertThat(dateFromReport).isNotEmpty();

        SimpleDateFormat dateFormat = new SimpleDateFormat(ReporterUtils.getDateFormat());
        Date date = dateFormat.parse(dateFromReport);

        assertThat(date).isAfter(before).isBefore(after);
    }

}
