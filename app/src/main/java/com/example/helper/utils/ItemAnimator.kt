package com.example.helper.utils

import android.animation.Animator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.ui.archiveFeature.ArchivesAdapter
import com.example.helper.ui.archiveFeature.InventoryAdapter
import com.example.helper.ui.archiveFeature.NestedAdapter
import com.example.helper.utils.Constants.HEART_ANIM

class ItemAnimator: DefaultItemAnimator(){
    override fun canReuseUpdatedViewHolder(
        viewHolder: RecyclerView.ViewHolder, payloads: MutableList<Any>
    ): Boolean = true

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder) = true

    override fun recordPreLayoutInformation(
        state: RecyclerView.State,
        viewHolder: RecyclerView.ViewHolder,
        changeFlags: Int,
        payloads: MutableList<Any>
    ): ItemHolderInfo {

        if (changeFlags == FLAG_CHANGED) {
            for (payload in payloads) {
                if (payload as? Int == HEART_ANIM) {
                    val progress = (viewHolder as InventoryAdapter.NestedViewHolder).binding.likeAnim.progress
                    val liked = progress != 0f
                    return ProductItemHolderInfo(liked)
                }
            }
        }
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads)
    }


    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo, postInfo: ItemHolderInfo
    ): Boolean {

        val holder = newHolder as InventoryAdapter.NestedViewHolder

        if(preInfo is ProductItemHolderInfo){
            if(preInfo.wasLiked){
                // It was previously liked. Unlike it
                holder.binding.likeAnim.progress = 0f
            } else {
                // It wasn't previously liked. Start like animation
                holder.binding.likeAnim.apply {
                    addAnimatorListener(object : Animator.AnimatorListener{
                        override fun onAnimationStart(animation: Animator) {
                            TODO("Not yet implemented")
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            dispatchAnimationFinished(holder)
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            TODO("Not yet implemented")
                        }

                        override fun onAnimationRepeat(animation: Animator) {
                            TODO("Not yet implemented")
                        }


                    })
                    playAnimation()
                }
            }
            return true
        }

        return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
    }

    class ProductItemHolderInfo(val wasLiked: Boolean) : ItemHolderInfo()
}