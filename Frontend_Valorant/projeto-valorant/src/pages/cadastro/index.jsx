import styles from './cadastro.module.css'
import api from '../../services/api'
import { useRef } from 'react'
import {Link, useNavigate} from 'react-router-dom'



function Cadastro() {
  const navigate = useNavigate();

  const inputNome = useRef()
  const inputEmail = useRef()
  const inputSenha = useRef()

  async function createUsers() {
    try {
    await api.post('/auth/registrar', {
      nome: inputNome.current.value,
      email: inputEmail.current.value,
      senha: inputSenha.current.value
    });

      navigate('/login');

      // eslint-disable-next-line no-unused-vars
    } catch (err) {
      alert("Erro ao cadastrar.");
    }

  }


  return (
      <div className={styles.container}>
        <form className={styles.form}>
          <h1>Cadastro</h1>
          <input name='nome' placeholder='Digite seu nome' type='text' ref={inputNome}/>
          <input name='email' placeholder='Digite seu email' type='email' ref={inputEmail}/>
          <input name='senha' placeholder='Digite sua senha' type='text'ref={inputSenha}/>
          <button className={styles.cadastrar} type='button' onClick={createUsers}>Cadastrar</button>
          <Link to={"/login"} className={styles.linkLogin}>Voltar ao Login</Link>
        </form>
      </div>
  )
}

export default Cadastro
