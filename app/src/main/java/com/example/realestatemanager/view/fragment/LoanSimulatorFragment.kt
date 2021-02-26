package com.example.realestatemanager.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_loan_simulation.*

class LoanSimulatorFragment : Fragment(){

    var montantEmprunt = 0
    var dureeEmprunt = 0
    var taux :Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loan_simulation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        montant_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                montant_emprunté.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    montantEmprunt = seekBar.progress
                    var montantMensuel = ((montantEmprunt * (taux/100) + montantEmprunt)/dureeEmprunt)
                    if (taux == 0.0 && dureeEmprunt == 0){
                        mensualité.text = montantEmprunt.toString()
                    }else{
                         mensualité.setText(String.format("%.2f", "$montantMensuel"), TextView.BufferType.EDITABLE)
                    }
                }
            }
        })

        durée_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                durée_emprunt.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    dureeEmprunt = seekBar.progress
                    var montantMensuel = (montantEmprunt * (taux/100) + montantEmprunt)/dureeEmprunt
                    mensualité.setText(String.format("%.2f",montantMensuel), TextView.BufferType.EDITABLE)                }
            }

        })

        taux_seekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var decimalTaux : Double = progress/10.0
                taux_emprunt.text = decimalTaux.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    taux = seekBar.progress/10.0
                    var montantMensuel = ((montantEmprunt * (taux/100)) + montantEmprunt)/dureeEmprunt
                    mensualité.setText(String.format("%.2f",montantMensuel), TextView.BufferType.EDITABLE)
                    Log.d("TAG", "taux : $taux ")
                    Log.d("TAG", "mensualité : $montantMensuel ")


                }
            }
        })

    }


}



