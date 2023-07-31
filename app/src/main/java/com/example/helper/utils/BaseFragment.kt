package com.example.helper.utils


import androidx.fragment.app.Fragment


//abstract class BaseFragment<State : ViewState, Event : ViewEvent, Action : ViewAction,
//        VM : BaseViewModel<Event, Action, State>> : Fragment() {
 abstract class BaseFragment : Fragment() {
}
//
//    private lateinit var oldPrefLocaleCode : String
//    val storage: Storage by lazy {
//        (requireActivity().application as MyApp).storage
//    }
//
//    private fun resetTitle(){
//        try{
//            val label = requireActivity().packageManager.getActivityInfo(requireActivity().componentName, PackageManager.GET_META_DATA).labelRes
//            if(label != 0){
//                requireActivity().setTitle(label)
//            }
//        } catch (e: PackageManager.NameNotFoundException){}
//    }
//
////    override fun onAttach(context: Context) {
////        oldPrefLocaleCode = Storage(context).getPreferredLocale()
////        requireActivity().applyOverrideConfiguration(LocaleUtil.getLocalizedConfiguration(oldPrefLocaleCode))
////        super.onAttach(context)
////    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        resetTitle()
//    }
//
//    override fun onResume() {
//        val currentLocaleCode = Storage(requireActivity()).getPreferredLocale()
//        if(oldPrefLocaleCode != currentLocaleCode){
//            requireActivity().recreate()
//            oldPrefLocaleCode = currentLocaleCode
//        }
//        super.onResume()
//    }
//}

//    override fun onResume() {
//        super.onResume()
//        val onBackPressedCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                childFragmentManager.popBackStack()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
//    }
//    override fun onStart() {
//        super.onStart()
//        if (activity is MainActivity) {
//            var mainActivity = activity as MainActivity
//            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
//        }
//    }
//    override fun onResume() {
//        super.onResume()
//        if (activity is MainActivity) {
//            (activity as MainActivity).setBottomNavigationVisibility(bottomNavigationViewVisibility)
//        }
//    }
