import {useEffect, useState} from 'react';
import styles from './Checkout.module.css';

export const Checkout = () => {
    const [loading, setLoading] = useState(false);
    const [userId, setUserId] = useState(null);

    useEffect(() => {
        const storedId = localStorage.getItem("userId");
        if (storedId) {
            setUserId(Number(storedId)); // Converte string para número
        }
    }, []);

    const handlePayment = async () => {
        setLoading(true);

        if (!userId) {
            alert("Erro: Usuário não identificado. Faça login novamente.");
            setLoading(false);
            return;
        }

        try {
            const response = await fetch("http://localhost:8082/validate/payment", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    userId: userId,
                    totalAmount: 70.00,
                    payer: { email: "cliente@teste.com" },
                    items: [
                        { title: "Pacote Conteúdo Valorant", quantity: 1, unit_price: 70.00 }
                    ]
                })
            });

            if (!response.ok) {
                throw new Error("Falha na comunicação com o servidor.");
            }

            const data = await response.json();

            if (data.redirectUrl) {
                window.location.href = data.redirectUrl;
            } else {
                alert("Erro: URL de pagamento não recebida.");
            }

        } catch (error) {
            console.error(error);
            alert("Ocorreu um erro ao iniciar o pagamento. Tente novamente.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>

            {/* Efeitos de Fundo - Note o uso de template literals para múltiplas classes */}
            <div className={`${styles.glowEffect} ${styles.glowRed}`} />
            <div className={`${styles.glowEffect} ${styles.glowBlue}`} />

            <div className={styles.card}>

                <div className={styles.headerStripe} />

                <div className={styles.content}>

                    <div className={styles.headerSection}>
                        <h1 className={styles.headerTitle}>Finalizar Pedido</h1>
                        <p className={styles.headerSubtitle}>
                            Complete a transação para liberar seu inventário.
                        </p>
                    </div>

                    {/* Aviso */}
                    <div className={styles.warningBox}>
                        <div className={styles.warningIcon}>
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                                <path strokeLinecap="round" strokeLinejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                            </svg>
                        </div>
                        <div>
                            <h3 className={styles.warningTitle}>Acesso Restrito</h3>
                            <p className={styles.warningText}>
                                Para garantir segurança e exclusividade, o acesso ao conteúdo premium só é liberado após a confirmação do pagamento.
                            </p>
                        </div>
                    </div>

                    {/* Resumo */}
                    <div className={styles.summaryBox}>
                        <div className={styles.summaryRow}>
                            <span className={styles.textGray}>Pacote Conteúdo Valorant</span>
                            <span className={`${styles.textWhite} ${styles.fontBold}`}>R$ 70,00</span>
                        </div>
                        <div className={styles.summaryRow}>
                            <span className={`${styles.textGray} ${styles.textSmall}`}>Taxas</span>
                            <span className={styles.textGreen}>Grátis</span>
                        </div>
                        <div className={`${styles.summaryRow} ${styles.totalRow}`}>
                            <span className={`${styles.textWhite} ${styles.fontBold} ${styles.textLarge}`}>Total</span>
                            <span className={`${styles.textWhite} ${styles.fontBold} ${styles.textXLarge}`}>R$ 70,00</span>
                        </div>
                    </div>

                    {/* Botão */}
                    <button
                        onClick={handlePayment}
                        disabled={loading}
                        className={styles.payButton}
                    >
                        {loading ? (
                            <>
                                <span className={styles.spinner}></span>
                                Processando...
                            </>
                        ) : (
                            "Pagar com Mercado Pago"
                        )}
                    </button>

                    {/* Footer */}
                    <div className={styles.secureFooter}>
                        <svg xmlns="http://www.w3.org/2000/svg" className={styles.secureIcon} fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                        </svg>
                        Ambiente Seguro e Criptografado
                    </div>

                </div>
            </div>
        </div>
    );
};

export default Checkout;