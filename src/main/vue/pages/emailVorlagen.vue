<script>
import {getVorlagen, sendVorlagen} from "@/main/vue/api/vorlage";

export default {
    data() {
        return {
            emailTemplates: [
                { vorlagenId: "1", title: "Passwort vergessen", subject: "Password Reset", body: "Hello,\n\nYou have requested to reset your password. "
                        + "Please click on the link below to reset your password:\n\n"
                        + "${link}" + "\n\nIf you did not request a password reset, please ignore this email."},
                { vorlagenId: "2", title: "Gleisbauer", subject: "Repair Order for TrackBuilder", body: "Hello,\n\nYou have a new repair order. "
                        + "Please click on the link below to open the repair order:\n\n" + "${Link}"},
            ],
            selectedTemplate: null,
            showPopUp: false,
            placeholders: [
                { key: "${Auftragsnummer}", description: "Die ID eines Auftrags (nur für den Gleisbauer)" },
                { key: "${Link}", description: "Der Link für den Gleisbauer" },
            ],
        };
    },

    methods: {
        async openPopUp(template) {
            this.selectedTemplate = { ...template };
            const vorlagenId = this.selectedTemplate.vorlagenId;
            this.showPopUp = true;

            try {
                const aktuellesVorlagenArray = await getVorlagen(vorlagenId);
                const aktuelleVorlage = aktuellesVorlagenArray.find(vorlage => vorlage.vorlagenId === vorlagenId);
                console.log(aktuelleVorlage)

                if (aktuelleVorlage) {
                    this.selectedTemplate.subject = aktuelleVorlage.stringSubject;
                    this.selectedTemplate.body = aktuelleVorlage.stringBody;
                } else {
                    console.log("Ich sollte nicht hier sein")
                }
            } catch (error) {
                console.error("Fehler beim Abrufen der Vorlage:", error);
            }
        },
        closePopUp() {
            this.showPopUp = false;
            this.selectedTemplate = null;
        },
        saveTemplate() {
            const vorlagenId = this.selectedTemplate.vorlagenId;
            const stringSubject = this.selectedTemplate.subject;
            const stringBody = this.selectedTemplate.body;
            console.log(vorlagenId);
            console.log(stringSubject);
            console.log(stringBody);
            sendVorlagen(vorlagenId, stringSubject, stringBody);
            this.closePopUp();
        }
    }
}

</script>

<template>
    <div>
        <ul>
            <li v-for="template in emailTemplates" :key="template.vorlagenId" @click="openPopUp(template)">
                <h3>{{ template.title }}</h3>
            </li>
        </ul>

        <div v-if="showPopUp" class="modal">
            <div class="modal-content">
                <span class="close" @click="closePopUp">&times;</span>
                <h2>Vorlage bearbeiten: {{ selectedTemplate.title }}</h2>
                <label for="subject">Betreff:</label>
                <input type="text" v-model="selectedTemplate.subject" id="subject" />

                <label for="body">Body:</label>
                <textarea v-model="selectedTemplate.body" id="body"></textarea>

                <div>
                    <h2>Verfügbare Platzhalter:</h2>
                    <ul>
                        <li v-for="placeholder in placeholders" :key="placeholder.key">
                            <strong>{{ placeholder.key }}</strong>: {{ placeholder.description }}
                        </li>
                    </ul>
                </div>


                <button @click="saveTemplate">Speichern</button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.modal {
    display: block;
    position: fixed;
    z-index: 1;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgb(0, 0, 0);
    background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
    background-color: #fefefe;
    margin: 15% auto;
    padding: 20px;
    border: 1px solid #888;
    width: 80%;
}

.modal-content h2 {
    font-size: 2.2em;
    margin-bottom: 10px;
}
label {
    font-size: 1.2em;
}

button {
    font-size: 1.4em;
    margin-top: 20px;
}


.close {
    color: #aaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: black;
    text-decoration: none;
    cursor: pointer;
}

input[type="text"], textarea {
    width: 100%;
    padding: 10px;
    margin: 10px 0;
    box-sizing: border-box;
    font-size: 1.2em;
}
textarea {
    height: 150px;
    min-height: 100px;
}
li h3 {
    font-size: 1.5em;
}
</style>
