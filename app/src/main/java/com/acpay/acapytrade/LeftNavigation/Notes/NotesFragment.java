package com.acpay.acapytrade.LeftNavigation.Notes;

import static com.acpay.acapytrade.MainActivity.getAPIHEADER;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.Cordinations.ECBackgroundSwitcherView;
import com.acpay.acapytrade.Cordinations.ECCardData;
import com.acpay.acapytrade.Cordinations.ECPagerView;
import com.acpay.acapytrade.Cordinations.ECPagerViewAdapter;
import com.acpay.acapytrade.Cordinations.ItemsCountView;
import com.acpay.acapytrade.MainActivity;
import com.acpay.acapytrade.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotesFragment extends Fragment {

    FrameLayout frameLayout;
    private ECPagerView ecPagerView;
    private String ApiUrl = "";

    public NotesFragment() {
    }

    public NotesFragment(FrameLayout frameLayout) {

        super();
        this.frameLayout = frameLayout;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        frameLayout.setLayoutParams(params);
    }

    FloatingActionButton notesPlacesListAdd;
    FloatingActionButton notesPlacesListAddDetails;
    NotesPlaces cardData;
    ECPagerViewAdapter adapter;
    View rootview;
    String TokenList;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_notes, container, false);

        progressBar = (ProgressBar) rootview.findViewById(R.id.notesProgress);
        progressBar.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(getContext())
                .build());

        notesPlacesListAdd = (FloatingActionButton) rootview.findViewById(R.id.notesPlacesListAdd);
        notesPlacesListAddDetails = (FloatingActionButton) rootview.findViewById(R.id.notesPlacesListAddDetails);
        notesPlacesListAdd.setVisibility(View.VISIBLE);
        notesPlacesListAddDetails.setVisibility(View.GONE);
        notesPlacesListAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddPlace.class));
            }
        });
        starting();
        setHasOptionsMenu(true);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (ecPagerView != null) {
                    if (!ecPagerView.collapse()) {

                        startActivity(new Intent(getContext(), MainActivity.class));
                    } else {
                        ecPagerView.toggle();
                    }
                } else {
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Places");
        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        starting();
    }

    private void starting() {

        String api = getAPIHEADER(getContext())+"/getNotes.php";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        TokenList = response;

                        List<ECCardData> ecCardData = extractFeuterFromJason(TokenList);

                        if (ecCardData.size() == 0) {
                            progressBar.setVisibility(View.GONE);

                        } else {
                            View viewById = rootview.findViewById(R.id.ec_bg_switcher_element);
                            viewById.setForeground(getResources().getDrawable(R.drawable.background_gradient));
                            progressBar.setVisibility(View.GONE);
                            adapter = new ECPagerViewAdapter(getActivity().getApplicationContext(), extractFeuterFromJason(TokenList)) {
                                @Override
                                public void instantiateCard(LayoutInflater inflaterService, ViewGroup head, ListView list, final ECCardData data) {
                                    cardData = (NotesPlaces) data;

                                    List<NotesPlacesDetails> listItems = cardData.getListItems();
                                    for(NotesPlacesDetails a : listItems){
                                        Log.e(a.getDeviceName(),a.getId()+"   " +cardData.getPlaceId());

                                    }
                                    CommentArrayAdapter commentArrayAdapter = new CommentArrayAdapter(getContext(), listItems);
                                    list.setAdapter(commentArrayAdapter);
                                    list.setDivider(getResources().getDrawable(R.drawable.list_divider));
                                    list.setDividerHeight((int) pxFromDp(getActivity().getApplicationContext(), 0.5f));
                                    list.setSelector(R.color.transparent);
                                    list.setBackgroundColor(Color.WHITE);
                                    list.setCacheColorHint(Color.TRANSPARENT);
                                    // list.setEmptyView();
                                    View gradient = new View(getActivity().getApplicationContext());
                                    gradient.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
                                    gradient.setBackgroundDrawable(getResources().getDrawable(R.drawable.card_head_gradient));
                                    head.addView(gradient);

                                    inflaterService.inflate(R.layout.activity_notes_header, head);

                                    TextView title = (TextView) head.findViewById(R.id.title);
                                    title.setText(cardData.getPalaceName());

                                    TextView placeName = (TextView) head.findViewById(R.id.placeName);
                                    placeName.setText(cardData.getPalaceName());
                                    TextView placeLocation = (TextView) head.findViewById(R.id.placeLocation);
                                    placeLocation.setText(cardData.getPalaceLocation());
                                    TextView placeDetails = (TextView) head.findViewById(R.id.placeDetails);
                                    placeDetails.setText(cardData.getPalaceDetails());
                                    TextView socialViewsCount = (TextView) head.findViewById(R.id.socialViewsCount);
                                    socialViewsCount.setText(String.valueOf(listItems.size()));
                                    ImageView avatar = (ImageView) head.findViewById(R.id.avatar);
                                    avatar.setImageResource(cardData.getImage());

                                    TextView edite = (TextView) head.findViewById(R.id.editeCard);
                                    edite.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getContext(), AddPlace.class);
                                            intent.putExtra("id", ((NotesPlaces) data).getPlaceId());
                                            intent.putExtra("name", ((NotesPlaces) data).getPalaceName());
                                            intent.putExtra("location", ((NotesPlaces) data).getPalaceLocation());
                                            intent.putExtra("detals", ((NotesPlaces) data).getPalaceDetails());
                                            startActivity(intent);
                                            // Toast.makeText(getContext(), "edite" + ((NotesPlaces) data).getPlaceId(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    TextView delete = (TextView) head.findViewById(R.id.deleteCard);
                                    delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setMessage("سيتم حذف المكان بكامل بياناته هل انت متاكد؟")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                            String api =  getAPIHEADER(getContext())+"/deleteNotes.php?id=" +((NotesPlaces) data).getPlaceId();
                                                            RequestQueue queue = Volley.newRequestQueue(getContext());
                                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                                                    new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            if (response.contains("1")) {
                                                                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                                                                starting();
                                                                            } else {
                                                                            }
                                                                        }
                                                                    }, new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {

                                                                    Log.e("onResponse", error.toString());
                                                                }
                                                            });
                                                            stringRequest.setShouldCache(false);
                                                            stringRequest.setShouldRetryConnectionErrors(true);
                                                            stringRequest.setShouldRetryServerErrors(true);
                                                            queue.add(stringRequest);

                                                        }
                                                    })
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            final AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                    });
                                    head.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            ecPagerView.toggle();
                                            Log.e("pos", ((NotesPlaces) data).getPlaceId());
                                            if (notesPlacesListAdd.getVisibility() == View.GONE) {
                                                notesPlacesListAdd.setVisibility(View.VISIBLE);
                                                notesPlacesListAddDetails.setVisibility(View.GONE);
                                            } else {
                                                notesPlacesListAdd.setVisibility(View.GONE);
                                                notesPlacesListAddDetails.setVisibility(View.VISIBLE);

                                                notesPlacesListAddDetails.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(getContext(), AddDevice.class);
                                                        intent.putExtra("id", ((NotesPlaces) data).getPlaceId());
                                                        startActivity(intent);
                                                    }
                                                });
                                            }

                                        }
                                    });
                                }


                            };
                            ecPagerView = (ECPagerView) rootview.findViewById(R.id.ec_pager_element);

                            ecPagerView.setPagerViewAdapter(adapter);
                            ecPagerView.setBackgroundSwitcherView((ECBackgroundSwitcherView) rootview.findViewById(R.id.ec_bg_switcher_element));

                            final ItemsCountView itemsCountView = (ItemsCountView) rootview.findViewById(R.id.items_count_view);
                            itemsCountView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                    Log.e("swip", "no");

                                }
                            });
                            ecPagerView.setOnCardSelectedListener(new ECPagerView.OnCardSelectedListener() {
                                @Override
                                public void cardSelected(int newPosition, int oldPosition, int totalElements) {
//                                    Log.e("swip", "ok");
                                    itemsCountView.update(newPosition, oldPosition, totalElements);

                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("onResponse", error.toString());
            }
        });
        stringRequest.setShouldCache(false);
        stringRequest.setShouldRetryConnectionErrors(true);
        stringRequest.setShouldRetryServerErrors(true);
        queue.add(stringRequest);



    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public List<ECCardData> extractFeuterFromJason(String jason) {
        List<ECCardData> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jason);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                List<int[]> picat = getRandomPictures();
                int[] pict = picat.get(0);
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                NotesPlaces notesPlaces;
                if (jsonArrayId.has("list")) {
                    JSONObject transitionsjsonObject = new JSONObject(jsonArrayId.getString("list"));
                    JSONArray transitionssa = transitionsjsonObject.names();
                    List<NotesPlacesDetails> comments = new ArrayList<>();
                    for (int x = 0; x < transitionssa.length(); x++) {
                        JSONObject transitionsjsonArrayId = transitionsjsonObject.getJSONObject(transitionssa.get(x).toString());
                        comments.add(new NotesPlacesDetails(transitionsjsonArrayId.getString("id")
                                , transitionsjsonArrayId.getString("name")
                                , transitionsjsonArrayId.getString("type")
                                , transitionsjsonArrayId.getString("model")
                                , transitionsjsonArrayId.getString("details")
                                , transitionsjsonArrayId.getString("ip")
                                , transitionsjsonArrayId.getString("username")
                                , transitionsjsonArrayId.getString("password")
                                , transitionsjsonArrayId.getString("port")
                                , transitionsjsonArrayId.getString("email")
                                , transitionsjsonArrayId.getString("email_pass")));
                    }
                    notesPlaces = new NotesPlaces(jsonArrayId.getString("id"),
                            jsonArrayId.getString("name"),
                            jsonArrayId.getString("location"),
                            jsonArrayId.getString("details"), pict[0], pict[1],
                            comments
                    );
                    list.add(notesPlaces);
                } else {
                    notesPlaces = new NotesPlaces(jsonArrayId.getString("id"),
                            jsonArrayId.getString("name"),
                            jsonArrayId.getString("location"),
                            jsonArrayId.getString("details"), pict[0], pict[1],
                            new ArrayList<NotesPlacesDetails>()
                    );
                    list.add(notesPlaces);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<int[]> getRandomPictures() {
        List<int[]> list = new ArrayList<>();
        list.add(new int[]{R.drawable.attractions, R.drawable.attractions_head});
        list.add(new int[]{R.drawable.city_scape, R.drawable.city_scape_head});
        list.add(new int[]{R.drawable.nature, R.drawable.nature_head});
        list.add(new int[]{R.drawable.night_life, R.drawable.night_life_head});
        list.add(new int[]{R.drawable.pic1, R.drawable.pic1});
        list.add(new int[]{R.drawable.pic2, R.drawable.pic2});
        list.add(new int[]{R.drawable.pic3, R.drawable.pic3});
        list.add(new int[]{R.drawable.pic4, R.drawable.pic4});
        list.add(new int[]{R.drawable.pic5, R.drawable.pic5});
        list.add(new int[]{R.drawable.pic6, R.drawable.pic6,});
        list.add(new int[]{R.drawable.pic7, R.drawable.pic7});

        Collections.shuffle(list);
        return list.subList(0, 1);
    }

}
