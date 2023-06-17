package com.example.bebergua_lembrete

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.bebergua_lembrete.Model.CalcularIngestaoDiaria
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var edit_peso: EditText
    private lateinit var edit_idade: EditText
    private lateinit var bt_calcular: Button
    private lateinit var txt_resultado_ml: TextView
    private lateinit var ic_atualizar: ImageView
    private lateinit var bt_alarme: Button
    private lateinit var bt_lembrete: Button
    private lateinit var tv_hora: TextView
    private lateinit var tv_minutos: TextView

    private lateinit var calcularingestaoDiaria: CalcularIngestaoDiaria
    private var resultadoMl = 0.0

    lateinit var timePickerDialog: TimePickerDialog
    lateinit var calendario: Calendar
    var horaAtual = 0
    var minutosAtuais = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        IniciarComponentes()
        calcularingestaoDiaria = CalcularIngestaoDiaria()

        bt_calcular.setOnClickListener {
            if (edit_peso.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.toast_Irforme_peso, Toast.LENGTH_SHORT).show()
            } else if (edit_idade.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.toast_Irforme_idade, Toast.LENGTH_SHORT).show()
            } else {
                val peso = edit_peso.text.toString().toDouble()
                val idade = edit_idade.text.toString().toInt()
                calcularingestaoDiaria.CalcularTotalMl(peso, idade)
                resultadoMl = calcularingestaoDiaria.ResultadoMl()
                val formatar = NumberFormat.getNumberInstance(Locale("pt", "BR"))
                formatar.isGroupingUsed = false
                txt_resultado_ml.text = formatar.format(resultadoMl) + " " + "ml"
            }
        }
        ic_atualizar.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(R.string.alert_title)
                .setMessage(R.string.alert_desc)
                .setPositiveButton("OK", { dialogInterface, i ->
                    edit_peso.setText("")
                    edit_idade.setText("")
                    txt_resultado_ml.text = ""
                })
            alertDialog.setNegativeButton("Cancel", { dialogInterface, i ->

            })
            val dialog = alertDialog.create()
            dialog.show()
        }
        bt_lembrete.setOnClickListener {
            calendario = Calendar.getInstance()
            horaAtual = calendario.get(Calendar.HOUR_OF_DAY)
            minutosAtuais = calendario.get(Calendar.MINUTE)
            timePickerDialog =
                TimePickerDialog(this,
                    { timePicker: TimePicker, hourOfDay: Int, minute: Int ->
                        tv_hora.text = String.format("%02d", hourOfDay)
                        tv_minutos.text = String.format("%02d", minute)
                    }, horaAtual, minutosAtuais, true
                )
            timePickerDialog.show()
        }
        bt_alarme.setOnClickListener {
            if (!tv_hora.toString().isEmpty() && !tv_minutos.text.toString().isEmpty()) {
                val intent=Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR,tv_hora.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MINUTES,tv_minutos.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MESSAGE,getString(R.string.alarm_message))

                startActivity(intent)

                if (intent.resolveActivity(packageManager)!=null){
                    startActivity(intent)
                }
            }
        }
    }

    private fun IniciarComponentes() {
        edit_peso = findViewById(R.id.etPeso)
        edit_idade = findViewById(R.id.etIdade)
        bt_calcular = findViewById(R.id.btCalcular)
        txt_resultado_ml = findViewById(R.id.txt_resultado_ml)
        ic_atualizar = findViewById(R.id.Atualizar)
        bt_lembrete = findViewById(R.id.btLembrete)
        bt_alarme = findViewById(R.id.btAlarme)
        tv_hora = findViewById(R.id.tvHora)
        tv_minutos = findViewById(R.id.tvMinutos)
    }
}
