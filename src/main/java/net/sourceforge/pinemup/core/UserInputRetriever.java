package net.sourceforge.pinemup.core;

import java.io.File;

public interface UserInputRetriever {
   String retrieveTextInputFromUser(String message);

   File retieveFileChoiceFromUser();

   String retrievePasswordFromUser();

   boolean retrieveUserConfirmation(String title, String message);

   void showErrorMessageToUser(String title, String message);

   void showInfoMessageToUser(String title, String message);
}
