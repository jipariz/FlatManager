import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
admin.initializeApp()

export const changePositionOfUsersInFlatAndNotifyOnTime = functions.pubsub.schedule('1 0 * * 1').onRun(context => {
    changePositionOfUsersInFlatAndNotify()
    .then(_ => {
        console.log("OnRequest: users changed successfuly")
    })
    .catch(error => {
        console.log("OnTime ERROR happened!")
        console.log(error)
    })
})

export const changePositionOfUsersInFlatAndNotifyOnRequest = functions.https.onRequest((request, response) => {
    changePositionOfUsersInFlatAndNotify()
    .then(_ => {
        console.log("OnRequest: users changed successfuly")
        response.send("Users changed successfuly!")
    })
    .catch(error => {
        console.log("OnRequest ERROR happened!")
        console.log(error)
        response.send(error)
    })
})

async function changePositionOfUsersInFlatAndNotify() {
    try {
        return admin.firestore().collection('flats').get()
        .then(flatSnapshots => {
            flatSnapshots.forEach(flatSnapshot => {
                const data = flatSnapshot?.data()?.usersList
                if (Array.isArray(data) && data.length) {
                    // Remove first item from array and place it at the end
                    const firstItem = data[0]
                    moveUserAndSetWeekCleanFinished(firstItem, flatSnapshot.id).catch(error => { return error })

                    // Notify user which have to clean this week
                    const newFirstUserId = (data[1] !== undefined) ? data[1].userId : data[0].userId
                    sendNotification(newFirstUserId, "Its your turn to clean up", "Hurraay, finally your flat will be clean").catch(error => { return error })
                }
            })
        }) 
    }
    catch (error) {
        return error;
    }
}

async function moveUserAndSetWeekCleanFinished(firstItem: any, flatId: string) {
    try {
        await admin.firestore().doc('flats/' + flatId).update({
            usersList: admin.firestore.FieldValue.arrayRemove(firstItem)
        }).catch(error => {return error})
        await admin.firestore().doc('flats/' + flatId).update({
            usersList: admin.firestore.FieldValue.arrayUnion(firstItem)
        }).catch(error => {return error})
        return admin.firestore().doc('flats/' + flatId).update({weekCleanFinished: false}).catch(error => { return error })
    }
    catch (err) {
        return "Moving users error!"
    }
}

async function sendNotification(userId: string, messageTitle: string, messageBody: string) {
    try {
        const userToken = (await admin.firestore().doc('users/' + userId).get()).data()?.token
        if (userToken !== undefined && userToken !== "") {
            // user has a token
            const message = {
                notification: {
                    title: messageTitle,
                    body: messageBody
                },
                token: userToken
           };
           const messageId = await admin.messaging().send(message)
           console.log("Message successfuly send, id => " + messageId)
           return messageId
        }
        return Promise
    }
    catch (err) {
        return "Notification sending error!"
    }
}

// export const testing = functions.https.onRequest((request, response) => {
//     let finalString:string = ""
//     admin.firestore().collection('flats').get()
//     .then(snapshot => {
//         snapshot.forEach(doc => {
//             finalString = finalString + " " + doc.id
//         });
//         response.send(finalString)
//       })
// })