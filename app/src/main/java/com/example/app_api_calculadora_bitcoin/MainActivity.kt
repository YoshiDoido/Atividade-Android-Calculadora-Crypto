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

    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker/"

    var cotacaoBitcoin: Double = 0.0

    private val TAG = "Lorem"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var btnCalcular = findViewById<Button>(R.id.btnCalcular)

        Log.v(TAG, "log de verbose")
        Log.d(TAG, "log de verbose")
        Log.i(TAG, "log de verbose")
        Log.w(TAG, "log de verbose")
        Log.e(TAG, "log de verbose", RuntimeException("teste de erro"))

        buscarCotacao()
        btnCalcular.setOnClickListener {
            calcular()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun buscarCotacao() {
        GlobalScope.async(Dispatchers.Default) {
            val resposta = URL(API_URL).readText()
            cotacaoBitcoin = JSONObject(resposta).getJSONObject("ticker").getDouble("last")

            val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            val cotacaoFormatada = f.format(cotacaoBitcoin)
            var txtCotacao = findViewById<TextView>(R.id.txtCotacao)
            txtCotacao.setText("$cotacaoFormatada")

            withContext(Main) {

            }
        }
    }

    fun calcular() {

        var txtValor = findViewById<EditText>(R.id.txtValor)

        if (txtValor.text.isEmpty()) {
            txtValor.error = "Preencha um valor"
            return
        }

        val valorDigitado = txtValor.text.toString().replace(",", ".").toDouble()

        val resultado = if (cotacaoBitcoin > 0) valorDigitado / cotacaoBitcoin else 0.0

        var txtQtdBitcoins = findViewById<TextView>(R.id.txtQtdBitcoins)

        txtQtdBitcoins.text = "%.8f".format(resultado)
    }
}