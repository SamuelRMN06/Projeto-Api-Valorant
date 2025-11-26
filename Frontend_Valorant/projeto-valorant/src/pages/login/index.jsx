import styles from './login.module.css'
import { Link, useNavigate } from 'react-router-dom'
import { useRef, useState } from 'react'
import api from '../../services/api'


function Login() {

  const inputEmail = useRef()
  const inputSenha = useRef()
  const navigate = useNavigate()

  const [loading, setLoading] = useState(false)

  async function loginUsers() {
    setLoading(true);

    try {
      const response = await api.post('/auth/login', {
        email: inputEmail.current.value,
        senha: inputSenha.current.value
      })

      const { token, premium, userId } = response.data;


      localStorage.setItem('token', token);
      localStorage.setItem('isPremium', premium);
      localStorage.setItem('userId', userId);


      if (premium === true) {

        navigate("/content");
      } else {

        alert("Sua conta foi criada, mas você precisa finalizar a assinatura Premium para acessar.");
        navigate("/checkout");
      }

    } catch (error) {
      console.error({ error });
      alert("Erro ao fazer login. Verifique email e senha.");
    } finally {
      setLoading(false);
    }
  }

  return (
      <div className={styles.containerLogin}>
        <form className={styles.formLogin}>
          <h1>Login</h1>
          <input
              name='email'
              placeholder='Digite seu email'
              type='email'
              ref={inputEmail}
          />
          <input
              name='senha'
              placeholder='Digite sua senha'
              type='password'
              ref={inputSenha}
          />

          <button
              className={styles.casdastrarLogin}
              type='button'
              onClick={loginUsers}
              disabled={loading}
          >
            {loading ? 'Carregando...' : 'Login'}
          </button>

          <Link to={"/cadastro"} className={styles.linkLogin}>Cadastrar</Link>
        </form>
      </div>
  )
}

export default Login