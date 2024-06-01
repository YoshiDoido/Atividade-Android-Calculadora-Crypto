package com.example.app_api_calculadora_bitcoin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject
import java.util.*
import java.net.URL
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    val API_URL_BITCOIN = "https://www.mercadobitcoin.net/api/BTC/ticker/"
    val API_URL_ETHERUM = "https://www.mercadobitcoin.net/api/ETH/ticker/"
    val API_URL_DOGECOIN = "https://www.mercadobitcoin.net/api/DOGE/ticker/"
    val API_URL_LITECOIN = "https://www.mercadobitcoin.net/api/LTC/ticker/"

    var cotacaoBitcoin: Double = 0.0
    var cotacaoEtherum: Double = 0.0
    var cotacaoDogecoin: Double = 0.0
    var cotacaoLitecoin: Double = 0.0

    private val TAG = "Lorem"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val txtValor = findViewById<EditText>(R.id.txtValor)
        val txtQtdBitcoins = findViewById<TextView>(R.id.txtQtdBitcoins)
        val txtQtdEtherum = findViewById<TextView>(R.id.txtQtdEtheruns)
        val txtQtdDogecoin = findViewById<TextView>(R.id.txtQtdDogecoins)
        val txtQtdLitecoin = findViewById<TextView>(R.id.txtQtdLitecoins)


        Log.v(TAG, "log de verbose")
        Log.d(TAG, "log de verbose")
        Log.i(TAG, "log de verbose")
        Log.w(TAG, "log de verbose")
        Log.e(TAG, "log de verbose", RuntimeException("teste de erro"))

        buscarCotacao()
        btnCalcular.setOnClickListener {
            calcular(txtValor, txtQtdBitcoins, txtQtdEtherum, txtQtdDogecoin, txtQtdLitecoin)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun buscarCotacao() {
        GlobalScope.async(Dispatchers.Default) {
            // Bitcoin
            val respostaBitcoin = URL(API_URL_BITCOIN).readText()
            cotacaoBitcoin = JSONObject(respostaBitcoin).getJSONObject("ticker").getDouble("last")

            // Etherum
            val respostaEtherum = URL(API_URL_ETHERUM).readText()
            cotacaoEtherum = JSONObject(respostaEtherum).getJSONObject("ticker").getDouble("last")

            // Dogecoin
            val respostaDogecoin = URL(API_URL_DOGECOIN).readText()
            cotacaoDogecoin = JSONObject(respostaDogecoin).getJSONObject("ticker").getDouble("last")

            // Litecoin
            val respostaLitecoin = URL(API_URL_LITECOIN).readText()
            cotacaoLitecoin = JSONObject(respostaLitecoin).getJSONObject("ticker").getDouble("last")

            val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            val cotacaoFormatadaBitcoin = f.format(cotacaoBitcoin)
            val cotacaoFormatadaEtherum = f.format(cotacaoEtherum)
            val cotacaoFormatadaDogecoin = f.format(cotacaoDogecoin)
            val cotacaoFormatadaLitecoin = f.format(cotacaoLitecoin)

            var txtCotacao = findViewById<TextView>(R.id.txtCotacao)
            txtCotacao.setText("$cotacaoFormatadaBitcoin, $cotacaoFormatadaEtherum, $cotacaoFormatadaDogecoin, $cotacaoFormatadaLitecoin")

            withContext(Main) {

            }
        }
    }

    fun calcular(txtValor: EditText, txtQtdBitcoins: TextView, txtQtdEtherum: TextView, txtQtdDogecoin: TextView, txtQtdLitecoin: TextView) {
        if (txtValor.text.isEmpty()) {
            txtValor.error = "Preencha um valor"
            return
        }

        val valorDigitado = txtValor.text.toString().replace(",", ".").toDouble()

        val resultadoBitcoin = if (cotacaoBitcoin > 0) valorDigitado / cotacaoBitcoin else 0.0
        val resultadoEtherum = if (cotacaoEtherum > 0) valorDigitado / cotacaoEtherum else 0.0
        val resultadoDogecoin = if (cotacaoDogecoin > 0) valorDigitado / cotacaoDogecoin else 0.0
        val resultadoLitecoin = if (cotacaoLitecoin > 0) valorDigitado / cotacaoLitecoin else 0.0

        txtQtdBitcoins.text = "%.8f".format(resultadoBitcoin)
        txtQtdEtherum.text = "%.8f".format(resultadoEtherum)
        txtQtdDogecoin.text = "%.8f".format(resultadoDogecoin)
        txtQtdLitecoin.text = "%.8f".format(resultadoLitecoin)
    }
}