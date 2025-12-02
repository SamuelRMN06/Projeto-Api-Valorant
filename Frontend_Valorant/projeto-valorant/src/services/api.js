import axios from 'axios'

const api = axios.create({
    baseURL: 'https://gateway-production-d435.up.railway.app'
})

export default api