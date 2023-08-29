package com.example.application830

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.application830.databinding.PositionRegisterDialogBinding

class PositionRegisterDialog(private val context : AppCompatActivity) {

    private lateinit var binding : PositionRegisterDialogBinding
    private val dig = Dialog(context)

    fun show(){

        binding = PositionRegisterDialogBinding.inflate(context.layoutInflater)

        dig.setContentView(binding.root)

        context.dialogResize(this,1.0f,0.4f)
        binding.registerYesBtn.setOnClickListener{
            //등록!
            dig.dismiss()
        }
        binding.registerNoBtn.setOnClickListener {
            dig.dismiss()
        }
        dig.show()

    }

    //dialog 크기조절
    fun Context.dialogResize(dialog: PositionRegisterDialog, width: Float, height: Float){
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30){
            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialog.dig.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()

            window?.setLayout(x, y)

        }else{
            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialog.dig.window
            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }
    }
}