package com.testing.backendless_messaging_test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.backendless.geo.geofence.IGeofenceCallback;
import com.testing.backendless_messaging_test.AppMode;
import com.testing.backendless_messaging_test.CustomCallback;
import com.testing.backendless_messaging_test.Defaults;
import com.testing.backendless_messaging_test.R;

import java.util.Iterator;
import java.util.List;

/**
 * Created by who on 4/21/15.
 */
public class FenceTest extends Activity {

    private EditText fenceNameField;
    private Button startFenceMonitoringButton;
    private Button stopFenceMonitoringButton;
    private Button goToMainMenuButton;
    private Switch useCustomCallbackToggle;
    private Switch runForEachFoundPointToggle;
    private Button runOnStayButton;
    private Button getPointsButton;
    private Button runOnEnterButton;
    private Button runOnExitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fence_test);

        initUI();
    }

    private void initUI() {
        fenceNameField = (EditText) findViewById(R.id.fence_name);
        useCustomCallbackToggle = (Switch) findViewById(R.id.customCallbackToggle);
        runForEachFoundPointToggle = (Switch) findViewById(R.id.run_for_each_point_toggle);
        startFenceMonitoringButton = (Button) findViewById(R.id.start_fence_monitoring_button);
        stopFenceMonitoringButton = (Button) findViewById(R.id.stop_fence_monitoring_button);
        runOnEnterButton = (Button) findViewById(R.id.run_onenter_button);
        runOnStayButton = (Button) findViewById(R.id.run_onstay_button);
        runOnExitButton = (Button) findViewById(R.id.run_onexit_button);
        getPointsButton = (Button) findViewById(R.id.get_points_button);
        goToMainMenuButton = (Button) findViewById(R.id.go_to_main_menu);

        startFenceMonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFenceMonitoring();
            }
        });

        stopFenceMonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopFenceMonitoring();
            }
        });

        runOnEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnEnterAction();
            }
        });

        runOnStayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnStayAction();
            }
        });

        runOnExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnExitAction();
            }
        });

        getPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPointsFromFence();
            }
        });

        goToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToMainMenuButtonClick();
            }
        });
    }

    private void startFenceMonitoring() {
        boolean useCustomCallback = useCustomCallbackToggle.isChecked();
        final String fenceName = fenceNameField.getText().toString();
        IGeofenceCallback customCallback = new CustomCallback();

        if ("".equals(fenceName)) {
            if (useCustomCallback) {
                Backendless.Geo.startGeofenceMonitoring(customCallback, new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void aVoid) {
                        Toast.makeText(FenceTest.this, "Fences monitoring started", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(FenceTest.this, "Failed: " + backendlessFault.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Backendless.Geo.startGeofenceMonitoring(new GeoPoint(10.1, 10.1), new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void aVoid) {
                        Toast.makeText(FenceTest.this, "Fences monitoring started", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(FenceTest.this, "Failed: " + backendlessFault.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            if (useCustomCallback) {
                Backendless.Geo.startGeofenceMonitoring(fenceName, customCallback, new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void aVoid) {
                        Toast.makeText(FenceTest.this, "Fence " + fenceName + " monitoring started", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(FenceTest.this, "Failed: " + backendlessFault.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Backendless.Geo.startGeofenceMonitoring(fenceName, new GeoPoint(), new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void aVoid) {
                        Toast.makeText(FenceTest.this, "Fence " + fenceName + " monitoring started", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(FenceTest.this, "Failed: " + backendlessFault.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }

    private void stopFenceMonitoring() {
        String fenceName = fenceNameField.getText().toString();

        if ("".equals(fenceName)) {
            try {
                Backendless.Geo.stopGeofenceMonitoring();
                Toast.makeText(FenceTest.this, "Success", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(FenceTest.this, "Failed " + e.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                Backendless.Geo.stopGeofenceMonitoring(fenceName);
                Toast.makeText(FenceTest.this, "Success. Fence name: " + fenceName, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(FenceTest.this, "Failed " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void runOnEnterAction() {

        final String fenceName = fenceNameField.getText().toString();

        if (!fenceNameIsEmpty()) {
            Backendless.Geo.runOnEnterAction(fenceName, new AsyncCallback<Integer>() {
                @Override
                public void handleResponse(Integer response) {
                    Toast.makeText(FenceTest.this, "Action ran for: " + response + " points", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(FenceTest.this, "Failed: " + fault.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void runOnStayAction() {
        final String fenceName = fenceNameField.getText().toString();

        if (!fenceNameIsEmpty()) {
            Backendless.Geo.runOnStayAction(fenceName, new AsyncCallback<Integer>() {
                @Override
                public void handleResponse(Integer response) {
                    Toast.makeText(FenceTest.this, "Action ran for: " + response + " points", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(FenceTest.this, "Failed: " + fault.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private void runOnExitAction() {

        final String fenceName = fenceNameField.getText().toString();

        if (!fenceNameIsEmpty()) {
            Backendless.Geo.runOnExitAction(fenceName, new AsyncCallback<Integer>() {
                @Override
                public void handleResponse(Integer response) {
                    Toast.makeText(FenceTest.this, "Action ran for: " + response + " points", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(FenceTest.this, "Failed: " + fault.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    // TODO: re-implement method for 4.0 version
    private void getPointsFromFence() {
//        final String fenceName = fenceNameField.getText().toString();
//        final Boolean runForEach = runForEachFoundPointToggle.isChecked();
//
//        if ("".equals(fenceName)) {
//            Toast.makeText(FenceTest.this, "Fence name is empty!", Toast.LENGTH_SHORT).show();
//        } else {
//            Backendless.Geo.getPoints(fenceName, new AsyncCallback<List<GeoPoint>>() {
//                @Override
//                public void handleResponse(List<GeoPoint> response) {
//                    int quantity = response.getTotalObjects();
//                    Toast.makeText(FenceTest.this, "Found " + quantity + " points", Toast.LENGTH_SHORT).show();
//
//                    if (runForEach && response.getTotalObjects() != 0) {
//                        Iterator<GeoPoint> iterator = response.getData().iterator();
//
//                        while (iterator.hasNext()) {
//                            GeoPoint point = iterator.next();
//                            // run action for each
//                            Backendless.Geo.runOnEnterAction(fenceName, point, new AsyncCallback<Void>() {
//                                @Override
//                                public void handleResponse(Void vvoid) {
//                                    Toast.makeText(FenceTest.this, "RunOnEnterAction passed", Toast.LENGTH_SHORT).show();
//                                }
//
//                                @Override
//                                public void handleFault(BackendlessFault backendlessFault) {
//                                    Toast.makeText(FenceTest.this, "RunOnEnterAction failed: " + backendlessFault.toString(), Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                            Backendless.Geo.runOnExitAction(fenceName, point, new AsyncCallback<Void>() {
//                                @Override
//                                public void handleResponse(Void aVoid) {
//                                    Toast.makeText(FenceTest.this, "RunOnExitAction passed", Toast.LENGTH_SHORT).show();
//                                }
//
//                                @Override
//                                public void handleFault(BackendlessFault backendlessFault) {
//                                    Toast.makeText(FenceTest.this, "RunOnExitAction failed: " + backendlessFault.toString(), Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                            Backendless.Geo.runOnStayAction(fenceName, point, new AsyncCallback<Void>() {
//                                @Override
//                                public void handleResponse(Void aVoid) {
//                                    Toast.makeText(FenceTest.this, "RunOnStayAction passed", Toast.LENGTH_SHORT).show();
//                                }
//
//                                @Override
//                                public void handleFault(BackendlessFault backendlessFault) {
//                                    Toast.makeText(FenceTest.this, "RunOnStayAction failed: " + backendlessFault.toString(), Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                        }
//                    }
//                }
//
//                @Override
//                public void handleFault(BackendlessFault fault) {
//                    Toast.makeText(FenceTest.this, "Failed: " + fault.toString(), Toast.LENGTH_LONG).show();
//                }
//            });
//        }

    }

    private void onGoToMainMenuButtonClick() {
        startActivity(new Intent(FenceTest.this, MainMenu.class));
    }

    private boolean fenceNameIsEmpty() {
        final String fenceName = fenceNameField.getText().toString();

        if ("".equals(fenceName)) {
            Toast.makeText(FenceTest.this, "Fence name is empty!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

}
