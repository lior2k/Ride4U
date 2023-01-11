/*eslint-disable*/
const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.removeUserFromDB = functions.https.onCall((data, response) => {
  const adminsRef = admin.database().ref("/admins/");
  const userRef = admin.database().ref("/users/" + data.userID);
  adminsRef.get(data.adminID).then((snapshot) => {
    if (snapshot.exists()) {
      admin.auth().deleteUser(data.authID)
        .then(() => {
          console.log(`Successfully deleted user with uid: ${data.authID}`);
        })
        .catch((error) => {
          console.error(error);
          return { status: 500 };
        });

      userRef.remove()
        .then(() => {
          console.log("Successfully deleted user:", data.userID);
        })
        .catch((error) => {
          console.log("Error deleting user:", error);
          return { status: 500 };
        });
      return { status: 201 };
    } else {
      return { status: 403 };
    }
  }).catch((error) => {
    console.error(error);
    return { status: 500 };
  });
  return data.authID;
});



