/*eslint-disable*/
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();




exports.sendNotification = functions.https.onCall((data, context) => {
  // extract the publisherID and username from the JSON data
  const publisherID = data.publisherID;
  const username = data.username;
  // get the device token for the publisher
  return admin.database().ref(`users/${publisherID}/devicetoken`).once('value').then(snapshot => {
      const deviceToken = snapshot.val();
      console.log(deviceToken);
      // create the message to send to the publisher
      const message = {
          notification: {
              title: "New Ride Passenger",
              body: `${username}  joined your ride.`
          },
          token: deviceToken
      };
      // send the message
      return admin.messaging().send(message).then(response => {
          return `Success: ${response}`;
      }).catch(error => {
          return `Error: ${error}`;
      });
  });
});
exports.postDeletedNotification = functions.https.onCall((data, context) => {
  // Get the list of device tokens from the data
  const ids = data.ids;
  const publisherName = data.publisherName;
  let deviceTokens = [];
  for(let i = 0 ; i < ids.length;i++){
    let userToken = admin.database().ref("/users/" + ids[i] + "/devicetoken");
    deviceTokens.push(userToken);
    }
  // Create the message to send
  const message = {
    notification: {
      title: "Ride Cancelled",
      body: `${publisherName} cancelled your ride.`,
    },
  };

  // Send the message to each device token
  // Send the message to the device tokens
  admin.messaging().sendToDevice(deviceTokens, message, (error, response) => {
    if (error) {
      console.log("Error sending messages:", error);
      return `Error sending messages: ${error}`;
    } else {
      console.log("Successfully sent messages:", response);
      return `Successfully sent messages to ${response.successCount} devices`;
    }
  });
});


// exports.postDeletedNotification = functions.https.onCall((data, context) => {
//   // extract the publisherID and username from the JSON data
//   const publisherName = data.publisherName;
//   const userID = data.userID;
//   // get the device token for the publisher
//   return admin.database().ref(`users/${userID}/devicetoken`).once('value').then(snapshot => {
//       const deviceToken = snapshot.val();
//       console.log(deviceToken);
//       // create the message to send to the publisher
//       const message = {
//           notification: {
//               title: "Ride Cancelled",
//               body: `${publisherName}  canceled your ride.`
//           },
//           token: deviceToken
//       };
//       // send the message
//       return admin.messaging().send(message).then(response => {
//           return `Success: ${response}`;
//       }).catch(error => {
//           return `Error: ${error}`;
//       });
//   });
// });



exports.LeaveNotification = functions.https.onCall((data, context) => {
  // extract the publisherID and username from the JSON data
  const publisherID = data.publisherID;
  const username = data.username;
  // get the device token for the publisher
  return admin.database().ref(`users/${publisherID}/devicetoken`).once('value').then(snapshot => {
      const deviceToken = snapshot.val();
      console.log(deviceToken);
      // create the message to send to the publisher
      const message = {
          notification: {
              title: "Passenger left",
              body: `${username} left your ride.`
          },
          token: deviceToken
      };
      // send the message
      return admin.messaging().send(message).then(response => {
          return `Success: ${response}`;
      }).catch(error => {
          return `Error: ${error}`;
      });
  });
});



exports.messageNotification = functions.https.onCall((data, context) => {
  // extract the publisherID and username from the JSON data
  const publisherID = data.publisherID;
  const username = data.username;
  const content = data.content;
  // get the device token for the publisher
  return admin.database().ref(`users/${publisherID}/devicetoken`).once('value').then(snapshot => {
      const deviceToken = snapshot.val();
      console.log(deviceToken);
      // create the message to send to the publisher
      const message = {
          notification: {
              title: `new message from ${username} `,
              body: `${content}`
          },
          token: deviceToken
      };
      // send the message
      return admin.messaging().send(message).then(response => {
          return `Success: ${response}`;
      }).catch(error => {
          return `Error: ${error}`;
      });
  });
});




// 


exports.scheduleNotification = functions.https.onCall((data, context) => {
  // Get the publisher ID and start time from the client
  const publisherID = data.publisherID;
  const startTime = new Date(data.startTime);
  
  // Subtract 15 minutes from the start time
  const scheduleTime = new Date(startTime.getTime() - (15 * 60 * 1000));
  
  // Get the device token from the Firebase Database
  return admin.database().ref(`users/${publisherID}/devicetoken`).once('value').then(snapshot => {
    const deviceToken = snapshot.val();
    const payload = {
      notification: {
          title: 'Reminder',
          body: 'Your ride is starting soon!',
      },
      schedule_time: scheduleTime
    };
    
    // Send the notification to the device
    return admin
        .messaging()
        .sendToDevice(deviceToken, payload)
        .then((response) => {
            return { result: `Successfully sent message: ${response}` }
        })
        .catch((error) => {
            return { error: `Error sending message: ${error}` }
        });
  });
});







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
















