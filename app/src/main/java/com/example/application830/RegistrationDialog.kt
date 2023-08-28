package com.example.application830

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.application830.databinding.RegistrationDialogBinding

class RegistrationDialog(private val context : AppCompatActivity) {

    private lateinit var binding : RegistrationDialogBinding
    private val dig = Dialog(context)
    private lateinit var listener : RequestDeleteBtnClickedListener


    fun show(str : String?){
        binding = RegistrationDialogBinding.inflate(context.layoutInflater)

        dig.setContentView(binding.root)

        //binding.(id).text = content // parentacticity 에서 전달받은 text
        context.dialogResize(this,1.0f,0.4f)
        binding.trashcanAddress.setText(str)
        binding.requestDeleteBtn.setOnClickListener{
            //삭제하는 거 작성 todo
            listener.ondeleteClicked("삭제를 눌렀습니다")
            dig.dismiss()
        }
        binding.cancleBtn.setOnClickListener {
            dig.dismiss()
        }
        dig.show()

    }
    fun setOnDeleteBtnClickedListener(listener:(String) -> Unit){
        this.listener = object: RequestDeleteBtnClickedListener {
            override fun ondeleteClicked(Content: String) {
                listener(Content)
            }
        }
    }

    //dialog 크기조절
    fun Context.dialogResize(dialog: RegistrationDialog, width: Float, height: Float){
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


    interface RequestDeleteBtnClickedListener {
        fun ondeleteClicked(Content:String)
    }
}