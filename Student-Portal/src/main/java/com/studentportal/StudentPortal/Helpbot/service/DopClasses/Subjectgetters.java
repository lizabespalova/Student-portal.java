package com.studentportal.StudentPortal.Helpbot.service.DopClasses;

import com.studentportal.StudentPortal.Helpbot.service.ConstClasses.Subjects;
import com.studentportal.StudentPortal.Helpbot.service.ConstClasses.Text;
import org.springframework.stereotype.Component;

@Component
public class Subjectgetters {
    private Text text;
    private Subjects subjects;
   public String getsubject(String messagetext){
       text = new Text();
       if(messagetext.equals(subjects.MATH.toString()))return text.getMatem_text();
       else if (messagetext.equals(subjects.LANGUAGES.toString()))return text.getLanguages_text();
       else if (messagetext.equals(subjects.MEDICINE.toString()))return text.getMedicine_text();
       else if (messagetext.equals(subjects.PHYLOSOPHY.toString()))return text.getPhylosophy_text();
       else if (messagetext.equals(subjects.PROGRAMMING.toString()))return text.getProgramming_text();
       else if (messagetext.equals(subjects.GEOGRAPHY.toString()))return text.getGeographe_text();
       else if (messagetext.equals(subjects.CHEMISTRY.toString()))return text.getChemistry_text();
       else return text.getAnother_text();
   }
}
