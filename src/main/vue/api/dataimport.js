import axios from 'axios'

export const search = async (pt) => {
    try {
        const response = await axios.get("http://localhost:8088/api/files/getFolder", {
            params: {p: pt}
        });
        return response.data
    }catch(error) {
    }

}

export const importFiles = async(s, st) => {
    try {
        const formData = new FormData();
        s.forEach(x => formData.append("paths", x));
        st.forEach(x => formData.append("ids", x));
       const response = await axios.post("http://localhost:8088/api/files/uploadFiles", formData);
       return response.data;
    } catch(error ) {

    }
}

export const deleteDataSets = async(formData) => {
    axios
        .delete("http://localhost:8080/api/files/deleteFiles", {
            data: formData,
            headers: {
                "Content-Type": "multipart/form-data",
            },
        })
        .then((response) => {
            console.log("Response:", response.data);
        })
        .catch((error) => {
            this.$q.notify({
                message: error.toString(),
                timeout: 5000,
            });
            console.error("Error:", error);
        });
}
