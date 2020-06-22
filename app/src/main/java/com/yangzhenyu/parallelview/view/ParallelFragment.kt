package com.yangzhenyu.parallelview.view

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yangzhenyu.parallelview.R
import java.lang.Exception

/**
 * 自定义的ParallelFragment
 * 重点！！！！
 * 这里面一个重要的操作就是克隆一个LayoutInflater然后解析自定义属性
 */
class ParallelFragment : Fragment() {

    private val mParallelViews:ArrayList<View> = arrayListOf()

   //自定义属性集合
    private val attrIds = intArrayOf(
        R.attr.a_in,
        R.attr.a_out,
        R.attr.x_in,
        R.attr.x_out,
        R.attr.y_in,
        R.attr.y_out
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 通过arguments拿到布局Id
        val bundle = arguments
        //克隆一个LayoutInflater
        /**
         * 此段内容 LayoutInflater类中的注释
         * To create a new LayoutInflater with an additional {@link Factory} for your
         * own views, you can use {@link #cloneInContext} to clone an existing
         * ViewFactory, and then call {@link #setFactory} on it to include your
         * Factory.
         * 通过注释我们知道：我们可以调用cloneInContext函数，生成一个自己的LayoutInflater
         * 同时我们还需要调用setFactory方法去监听view的解析
         */
        val newInflater = layoutInflater.cloneInContext(context)
        newInflater.apply {
            this.factory2 = ParallelFactory()
            return this.inflate(bundle!!.getInt("layoutId"), null)
        }
    }

    fun getParallelViews():ArrayList<View>{
        return mParallelViews
    }

    inner class ParallelFactory : LayoutInflater.Factory2{

        /**
         * 系统的view一般在：android.widget.  和 android.view.  这两个包里面
         * 我们需要区分自定义view和系统view
         * 然后通过反射的方式创建view
         */
        private val mClassPrefix = listOf<String>("android.widget.","android.view.")

        override fun onCreateView(
            parent: View?,
            name: String,
            context: Context,
            attrs: AttributeSet
        ): View? {
            var view :View? = null
            if (name.contains(".")){
                //自定义控件
                view = generateView(name,context, attrs)
            }else{
                //系统控件
                for (prefix in mClassPrefix){
                    view = generateView("$prefix$name",context,attrs)
                    if (view !=null){
                        break
                    }
                }
            }

            //解析自定义属性，并封装成一个模型
            /**
             * 此注释摘自Resources的obtainStyledAttributes方法
             * @param attrs The desired attributes to be retrieved. These attribute IDs must be sorted
             *              in ascending order.
             *              attrs必须按照升序排列好，非常重要！！！！！
             */
            val ta:TypedArray = context.obtainStyledAttributes(attrs,attrIds)
            if (ta.length()>0){
                val viewTag = ParallelViewTag(
                    ta.getFloat(0,0.0f),
                    ta.getFloat(1,0.0f),
                    ta.getFloat(2,0.0f),
                    ta.getFloat(3,0.0f),
                    ta.getFloat(4,0.0f),
                    ta.getFloat(5,0.0f)
                )
                view!!.setTag(R.id.parallel_view_tag,viewTag)
            }
            ta.recycle()
            mParallelViews.add(view!!)
            return view
        }

        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            return null
        }

        /**
         *通过包名的形式拿到类名
         * 然后获取类中两个参数的构造方法(context: Context, attrs: AttributeSet?)
         */
        private fun generateView(name:String, context: Context, attrs: AttributeSet):View?{
            try{
                val clazz = Class.forName(name)
                val constructor = clazz.getConstructor(Context::class.java,AttributeSet::class.java)
                return constructor.newInstance(context,attrs) as View
            }catch (e:Exception){
                e.printStackTrace()
            }
            return null
        }

    }
}
